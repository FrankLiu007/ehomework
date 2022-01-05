package name.wanghwx.ehomework.common.pojo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Environment;

import com.onyx.android.sdk.api.device.epd.EpdController;
import com.onyx.android.sdk.pen.data.TouchPoint;
import com.onyx.android.sdk.utils.CollectionUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

import name.wanghwx.ehomework.common.pojo.algebra.CubicBezier;
import name.wanghwx.ehomework.common.pojo.algebra.MathNumber;
import name.wanghwx.ehomework.common.pojo.algebra.Matrix;
import name.wanghwx.ehomework.common.pojo.algebra.Operable;
import name.wanghwx.ehomework.common.pojo.algebra.RealNumber;
import name.wanghwx.ehomework.common.pojo.algebra.RowVector;

/**
 * 笔画板
 * 1. 移动擦除需要严格保证笔画顺序
 * 2. 由于内存限制不能持有Bitmap实例
 * 3. 为保证渲染速度首次进入不能进行大量运算
 * 4. 为保证书写流畅进行需要实现并发
 */
public class StrokeBoard implements Serializable{

    private static final long serialVersionUID = 1L;

    private static final int DEFAULT_WIDTH = 1414;
    private static final int DEFAULT_HEIGHT = 6000;
    private static final int DEFAULT_CELL_WIDTH = 30;
    private static final int DEFAULT_CELL_HEIGHT = 30;
    private static final int MIN_HEIGHT = 707;
    private static final int MAX_BLANK = 50;

    private static final int MAX_POINT = 20;

    private static final Paint BLACK_STROKE = new Paint(){{
        this.setColor(Color.BLACK);
        this.setAntiAlias(true);
        this.setStyle(Style.STROKE);
        this.setDither(true);
        this.setStrokeCap(Cap.ROUND);
        this.setStrokeJoin(Join.ROUND);
    }};

    private static final Paint BLACK_FILL = new Paint(){{
        this.setColor(Color.BLACK);
        this.setAntiAlias(true);
        this.setStyle(Style.FILL);
        this.setDither(true);
        this.setStrokeCap(Cap.ROUND);
        this.setStrokeJoin(Join.ROUND);
    }};

    private static final Paint WHITE_BRUSH = new Paint(){{
        this.setColor(Color.WHITE);
        this.setAntiAlias(true);
        this.setStyle(Style.STROKE);
        this.setDither(true);
        this.setStrokeCap(Cap.ROUND);
        this.setStrokeJoin(Join.ROUND);
        this.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
    }};

    public static final StrokeBoard EMPTY_BOARD = new StrokeBoard(DEFAULT_WIDTH);

    private static boolean isValid(Stroke stroke){
        return stroke != null && stroke.mode != null && CollectionUtils.isNonBlank(stroke.path);
    }

    //手写范围定宽
    private final Integer fixedWidth;

    //手写板宽高
    private final Integer width;
    private final Integer height;

    //手写板网格宽高
    private final Integer cellWidth;
    private final Integer cellHeight;

    //笔画数据
    private final List<Stroke> strokes = new CopyOnWriteArrayList<>();
    private Cell[][] cells;

    public Integer getFixedHeight(){
        return fixedWidth*height/width;
    }

    public StrokeBoard(Integer fixedWidth){
        this(fixedWidth,DEFAULT_WIDTH,DEFAULT_HEIGHT);
    }
    private StrokeBoard(Integer fixedWidth,Integer width,Integer height){
        this(fixedWidth,width,height,DEFAULT_CELL_WIDTH,DEFAULT_CELL_HEIGHT);
    }
    private StrokeBoard(Integer fixedWidth,Integer width,Integer height,Integer cellWidth,Integer cellHeight){
        this.fixedWidth = fixedWidth;
        this.width = width;
        this.height = height;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        cells = new Cell[this.fixedWidth/this.cellWidth+1][this.height*this.fixedWidth/(this.width*this.cellHeight)+1];
    }

    public boolean isEmpty(){
        return strokes.isEmpty();
    }

    public void addAll(Collection<Stroke> collection){
        if(CollectionUtils.isNonBlank(collection)){
            for(Stroke stroke:collection){
                strokes.add(stroke);
                for(TouchPoint touchPoint:stroke.path){
                    int x = (int)Math.floor(touchPoint.x/cellWidth);
                    int y = (int)Math.floor((touchPoint.y+stroke.offsetY)/cellHeight);
                    if(cells[x][y] == null) cells[x][y] = new Cell();
                    cells[x][y].add(stroke);
                }
            }
        }
    }

    public void removeAll(Collection<Stroke> collection){
        if(CollectionUtils.isNonBlank(collection)){
            for(Stroke stroke:collection){
                strokes.remove(stroke);
            }
        }
    }

    public Step<Stroke> addStroke(Stroke stroke,final Canvas canvas){
        if(!isValid(stroke)) return null;
        strokes.add(stroke);
        for(TouchPoint touchPoint:stroke.path){
            int x = (int)Math.floor(touchPoint.x/cellWidth);
            int y = (int)Math.floor((touchPoint.y+stroke.offsetY)/cellHeight);
            if(cells[x][y] == null) cells[x][y] = new Cell();
            cells[x][y].add(stroke);
        }
        Path path;
        TouchPoint current,next = null;
        for(TouchPoint touchPoint:stroke.path){
            current = next;
            next = touchPoint;
            if(current != null && next != null){
                path = new Path();
                path.moveTo((float)getRealX(current),(float)getRealY(current,stroke.offsetY));
                path.lineTo((float)getRealX(next),(float)getRealY(next,stroke.offsetY));
                BLACK_STROKE.setStrokeWidth((float)getRealStrokeWidth(stroke.width,(current.pressure+next.pressure)/2));
                canvas.drawPath(path,BLACK_STROKE);
            }
        }
        return new Step<>(null,Collections.singleton(stroke));
    }

    public Step<Stroke> moveErase(final Stroke stroke,final Canvas canvas){
        if(!isValid(stroke)) return null;
        strokes.add(stroke);
        Path path;
        TouchPoint current,next = null;
        for(TouchPoint touchPoint:stroke.path){
            current = next;
            next = touchPoint;
            if(current != null && next != null){
                path = new Path();
                path.moveTo((float)getRealX(current),(float)getRealY(current,stroke.offsetY));
                path.lineTo((float)getRealX(next),(float)getRealY(next,stroke.offsetY));
                WHITE_BRUSH.setStrokeWidth((float)getRealStrokeWidth(stroke.width,(current.pressure+next.pressure)/2));
                canvas.drawPath(path,WHITE_BRUSH);
            }
        }
        return new Step<>(null,Collections.singleton(stroke));
    }

    public Step<Stroke> removeByStroke(Stroke stroke,final Canvas canvas){
        if(!isValid(stroke)) return null;
        List<Stroke> extra = new ArrayList<>();
        for(TouchPoint touchPoint:stroke.path){
            int x = (int)Math.floor(touchPoint.x/cellWidth);
            int y = (int)Math.floor((touchPoint.y+stroke.offsetY)/cellHeight);
            Cell cell = cells[x][y];
            if(cell == null || CollectionUtils.isNullOrEmpty(cell.strokes)) continue;
            for(Stroke s:cell.strokes){
                strokes.remove(s);
                extra.add(s);
                Path path;
                TouchPoint current,next = null;
                if(isValid(s)){
                    for(TouchPoint t:s.path){
                        current = next;
                        next = t;
                        if(current != null && next != null){
                            path = new Path();
                            path.moveTo((float)getRealX(current),(float)getRealY(current,s.offsetY));
                            path.lineTo((float)getRealX(next),(float)getRealY(next,s.offsetY));
                            WHITE_BRUSH.setStrokeWidth((float)getRealStrokeWidth(s.width,(current.pressure+next.pressure)/2));
                            canvas.drawPath(path,WHITE_BRUSH);
                        }
                    }
                }

            }
            cell.strokes.clear();
        }
        return CollectionUtils.isNonBlank(extra)?new Step<>(extra,null):null;
    }

    public Step<Stroke> clearBoard(){
        List<Stroke> extra = new ArrayList<>(strokes);
        strokes.clear();
        cells = new Cell[fixedWidth/cellWidth+1][height*fixedWidth/(width*cellHeight)+1];
        return CollectionUtils.isNonBlank(extra)?new Step<>(extra,null):null;
    }

    public Bitmap draw(){
        Bitmap bitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.TRANSPARENT,PorterDuff.Mode.CLEAR);
        for(Stroke stroke:strokes){
            if(stroke == null || stroke.mode == null) continue;
            Path path;
            TouchPoint current,next = null;
            switch(stroke.mode){
                case BRUSH:
                    for(TouchPoint touchPoint:stroke.path){
                        current = next;
                        next = touchPoint;
                        if(current != null && next != null){
                            path = new Path();
                            path.moveTo((float)getRealX(current),(float)getRealY(current,stroke.offsetY));
                            path.lineTo((float)getRealX(next),(float)getRealY(next,stroke.offsetY));
                            BLACK_STROKE.setStrokeWidth((float)getRealStrokeWidth(stroke.width,(current.pressure+next.pressure)/2));
                            canvas.drawPath(path,BLACK_STROKE);
                        }
                    }
                    break;
                case MOVE:
                    for(TouchPoint touchPoint:stroke.path){
                        current = next;
                        next = touchPoint;
                        if(current != null && next != null){
                            path = new Path();
                            path.moveTo((float)getRealX(current),(float)getRealY(current,stroke.offsetY));
                            path.lineTo((float)getRealX(next),(float)getRealY(next,stroke.offsetY));
                            WHITE_BRUSH.setStrokeWidth((float)getRealStrokeWidth(stroke.width,(current.pressure+next.pressure)/2));
                            canvas.drawPath(path,WHITE_BRUSH);
                        }
                    }
                    break;
                case STROKE:
                    break;
                default:
                    break;
            }
        }
        return bitmap;
    }

    public File saveAs(String filename){
        int minY = height;
        int maxY = 0;
        if(CollectionUtils.isNonBlank(strokes)){
            for(Stroke stroke:strokes){
                if(stroke == null || CollectionUtils.isNullOrEmpty(stroke.path)) continue;
                for(TouchPoint touchPoint:stroke.path){
                    int y = (int)Math.round((touchPoint.y+stroke.offsetY)*width/fixedWidth);
                    int y1 = y-MAX_BLANK > 0?y-MAX_BLANK:0;
                    int y2 = y+MAX_BLANK < height?y+MAX_BLANK:height;
                    if(minY > y1) minY = y1;
                    if(maxY < y2) maxY = y2;
                }

            }
        }
        if(maxY < minY) minY = 0;
        if(maxY < minY+MIN_HEIGHT) maxY = minY+MIN_HEIGHT;
        File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"Ehomework");
        File file = new File(path,filename);
        try{
            if((path.isDirectory() || path.mkdirs()) && (!file.isFile() || file.delete())) Bitmap.createBitmap(draw(),0,minY,width,maxY-minY).compress(Bitmap.CompressFormat.PNG,100,new FileOutputStream(file));
        }catch(IOException e){
            e.printStackTrace();
        }
        return file;
    }

    private Path buildPath(Stroke stroke){
        Path path = new Path();
        int size = stroke.path.size();
        int interval = (size-2)%MAX_POINT==0?(size-2)/MAX_POINT:(size-2)/MAX_POINT+1;
        int order = (size-2)/interval;
        Matrix<Operable> matrix = Matrix.getInstance(order,order+1);
        RowVector<RealNumber> beginVector = toRowVector(stroke.path.get(0),stroke.offsetY),currentVector,endVector = toRowVector(stroke.path.get(size-1),stroke.offsetY);
        for(int i=0;i<order;i++){
            for(int j=0;j<order+1;j++){
                if(j == order){
                    currentVector = toRowVector(stroke.path.get((i+1)*interval),stroke.offsetY);
                    if(i == order-1){
                        matrix.setElement(i,j,currentVector.multiply(new RealNumber(6)).subtract(endVector));
                    }else if(i == 0){
                        matrix.setElement(i,j,currentVector.multiply(new RealNumber(6)).subtract(beginVector));
                    }else{
                        matrix.setElement(i,j,currentVector.multiply(new RealNumber(6)));
                    }
                }else if(j == i){
                    matrix.setElement(i,j,new RealNumber(4));
                }else if(Math.abs(i-j) == 1){
                    matrix.setElement(i,j,new RealNumber(1));
                }else{
                    matrix.setElement(i,j,new RealNumber(0));
                }
            }
        }
        RealNumber realNumber;
        for(int j=0;j<order;j++){
            for(int i=j;i<order;i++){
                realNumber = (RealNumber)matrix.getElement(i,j);
                if(i == j && Math.abs(realNumber.getReal()-1) > MathNumber.DEFAULT_PRECISION){
                    matrix.rowTransform(j,new RealNumber(1.0/realNumber.getReal()),null,null);
                }
                if(i != j && Math.abs(realNumber.getReal()) > MathNumber.DEFAULT_PRECISION){
                    matrix.rowTransform(i,new RealNumber(1),j,new RealNumber(-realNumber.getReal()));
                }
            }
        }
        for(int j=order-1;j>-1;j--){
            for(int i=j;i>-1;i--){
                realNumber = (RealNumber)matrix.getElement(i,j);
                if(i == j && Math.abs(realNumber.getReal()-1) > MathNumber.DEFAULT_PRECISION){
                    matrix.rowTransform(j,new RealNumber(1.0/realNumber.getReal()),null,null);
                }
                if(i != j && Math.abs(realNumber.getReal()) > MathNumber.DEFAULT_PRECISION){
                    matrix.rowTransform(i,new RealNumber(1),j,new RealNumber(-realNumber.getReal()));
                }
            }
        }
        List<RowVector<RealNumber>> bVectors = new ArrayList<>();
        bVectors.add(beginVector);
        for(int i=0;i<order;i++){
            bVectors.add((RowVector<RealNumber>)matrix.getElement(i,order));
        }
        bVectors.add(endVector);
        List<CubicBezier> cubicBeziers = new ArrayList<>();
        RowVector<RealNumber> startVector,stopVector,firstVector,secondVector;
        for(int i=0;i<order+1;i++){
            startVector = i==0?beginVector:toRowVector(stroke.path.get((i)*interval),stroke.offsetY);
            stopVector = i==order?endVector:toRowVector(stroke.path.get((i+1)*interval),stroke.offsetY);
            firstVector = bVectors.get(i);
            secondVector = bVectors.get(i+1);
            cubicBeziers.add(new CubicBezier(startVector,
                    firstVector.multiply(new RealNumber(2.0/3)).add(secondVector.multiply(new RealNumber(1.0/3))),
                    firstVector.multiply(new RealNumber(1.0/3)).add(secondVector.multiply(new RealNumber(2.0/3))),
                    stopVector));
        }
        List<RowVector<RealNumber>> leftPoints = new ArrayList<>();
        List<RowVector<RealNumber>> rightPoints = new ArrayList<>();
        RowVector<RealNumber> beginLeftNormal,c1LeftNormal,c2LeftNormal,endLeftNormal;
        for(CubicBezier cubicBezier:cubicBeziers){
            beginLeftNormal = getLeftNormal(cubicBezier.getDBegin());
            c1LeftNormal = getLeftNormal(cubicBezier.getDC1());
            c2LeftNormal = getLeftNormal(cubicBezier.getDC2());
            endLeftNormal = getLeftNormal(cubicBezier.getDEnd());
            leftPoints.add(cubicBezier.getBegin().add(beginLeftNormal.multiply(new RealNumber(getRealStrokeWidth(stroke.width,cubicBezier.getBegin().getElement(2).getReal()/2)))));
            rightPoints.add(cubicBezier.getBegin().subtract(beginLeftNormal.multiply(new RealNumber(getRealStrokeWidth(stroke.width,cubicBezier.getBegin().getElement(2).getReal()/2)))));
            leftPoints.add(cubicBezier.getC1().add(c1LeftNormal.multiply(new RealNumber(getRealStrokeWidth(stroke.width,cubicBezier.getC1().getElement(2).getReal()/2)))));
            rightPoints.add(cubicBezier.getC1().subtract(c1LeftNormal.multiply(new RealNumber(getRealStrokeWidth(stroke.width,cubicBezier.getC1().getElement(2).getReal()/2)))));
            leftPoints.add(cubicBezier.getC2().add(c2LeftNormal.multiply(new RealNumber(getRealStrokeWidth(stroke.width,cubicBezier.getC2().getElement(2).getReal()/2)))));
            rightPoints.add(cubicBezier.getC2().subtract(c2LeftNormal.multiply(new RealNumber(getRealStrokeWidth(stroke.width,cubicBezier.getC2().getElement(2).getReal()/2)))));
            leftPoints.add(cubicBezier.getEnd().add(endLeftNormal.multiply(new RealNumber(getRealStrokeWidth(stroke.width,cubicBezier.getEnd().getElement(2).getReal()/2)))));
            rightPoints.add(cubicBezier.getEnd().subtract(endLeftNormal.multiply(new RealNumber(getRealStrokeWidth(stroke.width,cubicBezier.getEnd().getElement(2).getReal()/2)))));
        }
        path.moveTo((float)leftPoints.get(0).getElement(0).getReal(),(float)leftPoints.get(0).getElement(1).getReal());
        path.lineTo((float)rightPoints.get(0).getElement(0).getReal(),(float)rightPoints.get(0).getElement(1).getReal());
        for(int i=0;4*i<rightPoints.size();i++){
//            path.moveTo((float)rightPoints.get(4*i).getElement(0).getReal(),(float)rightPoints.get(4*i).getElement(1).getReal());
            path.cubicTo((float)rightPoints.get(4*i+1).getElement(0).getReal(),(float)rightPoints.get(4*i+1).getElement(1).getReal(),
                    (float)rightPoints.get(4*i+2).getElement(0).getReal(),(float)rightPoints.get(4*i+2).getElement(1).getReal(),
                    (float)rightPoints.get(4*i+3).getElement(0).getReal(),(float)rightPoints.get(4*i+3).getElement(1).getReal());
        }
//        path.moveTo((float)rightPoints.get(rightPoints.size()-1).getElement(0).getReal(),(float)rightPoints.get(rightPoints.size()-1).getElement(1).getReal());
        path.lineTo((float)leftPoints.get(leftPoints.size()-1).getElement(0).getReal(),(float)leftPoints.get(leftPoints.size()-1).getElement(1).getReal());
        for(int i=0,il=leftPoints.size();4*i<il;i++){
//            path.moveTo((float)rightPoints.get(il-4*i-1).getElement(0).getReal(),(float)rightPoints.get(il-4*i-1).getElement(1).getReal());
            path.cubicTo((float)leftPoints.get(il-4*i-2).getElement(0).getReal(),(float)leftPoints.get(il-4*i-2).getElement(1).getReal(),
                    (float)leftPoints.get(il-4*i-3).getElement(0).getReal(),(float)leftPoints.get(il-4*i-3).getElement(1).getReal(),
                    (float)leftPoints.get(il-4*i-4).getElement(0).getReal(),(float)leftPoints.get(il-4*i-4).getElement(1).getReal());
        }
        return path;
    }

    private double getRealX(TouchPoint touchPoint){
        return touchPoint.x*width/fixedWidth;
    }

    private double getRealY(TouchPoint touchPoint,double offsetY){
        return (touchPoint.y+offsetY)*width/fixedWidth;
    }

    private double getRealStrokeWidth(double strokeWidth,double pressure){
        return (strokeWidth+2)*Math.pow(pressure/EpdController.getMaxTouchPressure(),0.75)*width/fixedWidth;
    }

    private RowVector<RealNumber> toRowVector(TouchPoint touchPoint,double offsetY){
        return new RowVector<>(new RealNumber(getRealX(touchPoint)),new RealNumber(getRealY(touchPoint,offsetY)),new RealNumber(touchPoint.pressure));
    }

    private RowVector<RealNumber> getLeftNormal(RowVector<RealNumber> rowVector){
        double angle = Math.atan2(rowVector.getElement(1).getReal(),rowVector.getElement(0).getReal());
        return new RowVector<>(new RealNumber(-Math.sin(angle)),new RealNumber(Math.cos(angle)),new RealNumber(0));
    }

    private class Cell implements Serializable{

        private static final long serialVersionUID = 1L;

        private final Set<Stroke> strokes = new CopyOnWriteArraySet<>();

        private void add(Stroke stroke){
            strokes.add(stroke);
        }

    }

}