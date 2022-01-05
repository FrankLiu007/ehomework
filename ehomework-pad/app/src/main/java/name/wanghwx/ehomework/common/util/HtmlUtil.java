package name.wanghwx.ehomework.common.util;

import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;

import java.net.URL;

public class HtmlUtil{

    public static Spanned transform(String html){
        return html == null?null:Html.fromHtml(html,source->{
            Drawable drawable = null;
            try{
                URL url = new URL(source);
                drawable = Drawable.createFromStream(url.openStream(),"");
                drawable.setBounds(0, 0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
            }catch(Exception e){
                e.printStackTrace();
            }
            return drawable;
        },null);
    }

}
