package com.example.myapplication;

        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;

        import androidx.annotation.NonNull;

        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

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
        ImageView imageViewDelete = convertView.findViewById(R.id.imageViewDelete);
        TextView typeView = convertView.findViewById(R.id.txtTypeClothes);
        TextView idView = convertView.findViewById(R.id.txtIdClothes);
        //imageView.setImageResource(getItem(position).getImage());
        imageViewDelete.setImageResource(R.drawable.ic_delete);

        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("ourtest");
        imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String clothesIdString = getItem(position).getId();
                FirebaseDatabase.getInstance().getReference().child("Inventory").child(clothesIdString).child("status").setValue(0);
                //ArrayList<Event> eventList = new ArrayList<>();

                reference2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //eventList.clear();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            String dateString = snapshot1.getKey();
                            int status = ((Long) snapshot1.child("status").getValue()).intValue();
                            String idString = snapshot1.child("id").getValue().toString();
                            if(idString.equals(clothesIdString)){
                                FirebaseDatabase.getInstance().getReference().child("ourtest").child(dateString).child("status").setValue(0);
                            }
                        }
                        reference2.removeEventListener(this);//without this, once a delete icon is clicked, I was not able to reset associated event status back to 1.

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });






                            }
        });



        if(getItem(position).isStatusTrue()){
            typeView.setText("Type: " + getItem(position).getType());
            idView.setText("ID: " + getItem(position).getId());
            if(getItem(position).getType().equals("pants")){
                imageView.setImageResource(R.drawable.image_pants);
            }else if(getItem(position).getType().equals("dress")){
                imageView.setImageResource(R.drawable.image_dress);
            }else if(getItem(position).getType().equals("shirt")){
                imageView.setImageResource(R.drawable.image_shirt);
            }else if(getItem(position).getType().equals("shoes")){
                imageView.setImageResource(R.drawable.image_shoes);
            }else if(getItem(position).getType().equals("jacket")) {
                imageView.setImageResource(R.drawable.image_jacket);
            }
        }else{
            imageView.setImageResource(R.drawable.image_no_style);
        }





        return convertView;

    }

}

