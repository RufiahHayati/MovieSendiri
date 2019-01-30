package comw.example.rplrus26.moviesendiri;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class RViewHolders extends RecyclerView.ViewHolder {

    public TextView name;
    public ImageView gambar;
    public Button detail;
    public Button share, delete;

    public RViewHolders(View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.name);
        gambar = itemView.findViewById(R.id.imageView);
        detail = itemView.findViewById(R.id.btn_detail);
        share = itemView.findViewById(R.id.btn_Share);
        delete = itemView.findViewById(R.id.btn_delete);

    }
}
