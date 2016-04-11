package alvin.anglesearch.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by alvin on 2016/3/28.
 */
public class Bulk_Dialog extends Dialog {
    public Bulk_Dialog(Context context) {
        super(context);
    }

    public Bulk_Dialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder{
        private Context context;
        private Button positiveButton;
        private Button negativeButton;
        private EditText bulk;
        private EditText info;
        private DialogInterface.OnClickListener positiveButtonClickListener;
        private DialogInterface.OnClickListener negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }



    }

}
