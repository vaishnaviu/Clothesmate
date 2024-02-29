package com.example.myapplication;

        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;

        import androidx.annotation.NonNull;

        import java.util.ArrayList;

public class ClothesAdapter extends ArrayAdapter<Clothes> {
    private Context mContext;
    private int mResource;
    public ClothesAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Clothes> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;

    }



    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        convertView = layoutInflater.inflate(mResource,parent,false);

        ImageView imageView = convertView.findViewById(R.id.imageViewClothes);
        TextView typeView = convertView.findViewById(R.id.txtTypeClothes);
        TextView idView = convertView.findViewById(R.id.txtIdClothes);
        //imageView.setImageResource(getItem(position).getImage());
        if(getItem(position).isStatus()){
            typeView.setText(getItem(position).getType());
            idView.setText(getItem(position).getId());
            if(getItem(position).getType().equals("pants")){
                imageView.setImageResource(R.drawable.ic_launcher_background);
            }else if(getItem(position).getType().equals("dress")){
                imageView.setImageResource(R.drawable.ic_mycloset_press);
            }
        }


        return convertView;

    }

}

