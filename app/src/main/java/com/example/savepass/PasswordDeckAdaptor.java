package com.example.savepass;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

class PasswordDeckAdapter extends RecyclerView.Adapter<PasswordDeckAdapter.DeckViewHolder> {
    private ArrayList<QueryDocumentSnapshot> passwords =new ArrayList<>();
    private static String user_uid;
public static class DeckViewHolder extends RecyclerView.ViewHolder{

    private LinearLayout rowContainer;
    private TextView rowPlatform;
    private TextView rowUsername;
    private ImageView alertImg;
    DeckViewHolder(View v){
        super(v);
        rowContainer=v.findViewById(R.id.row_container);
        rowPlatform=v.findViewById(R.id.row_platform);
        rowUsername=v.findViewById(R.id.row_username);
        alertImg=v.findViewById(R.id.alertImage);
        alertImg.setVisibility(View.INVISIBLE);

        rowContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QueryDocumentSnapshot current=(QueryDocumentSnapshot) rowContainer.getTag();
                Intent intent =new Intent(v.getContext(),PasswordDetail.class);
                intent.putExtra("user_uid",PasswordDeckAdapter.user_uid);
                intent.putExtra("doc_id",current.getId());
                intent.putExtra("platform",current.get("platform").toString());
                intent.putExtra("username",current.get("Username").toString());
                intent.putExtra("password",current.get("password").toString());
                //intent.putExtra("published",current.get("published").toString());
                v.getContext().startActivity(intent);
            }
        });
    }


}

public PasswordDeckAdapter(ArrayList<QueryDocumentSnapshot> allPws,String user_uid){
    this.user_uid=user_uid;
    this.passwords=allPws;
    Log.d("came_here",""+allPws.size());
}

    @NonNull
    @Override
    public DeckViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.password_row,parent,false);
        return new DeckViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeckViewHolder holder, int position) {
        QueryDocumentSnapshot current=passwords.get(position);
        holder.rowPlatform.setText(current.get("platform").toString());
        holder.rowUsername.setText(current.get("Username").toString());

        Timestamp date=(Timestamp) current.get("published");
       if(isTimeToUpdate(date.toDate())){
            holder.alertImg.setVisibility(View.VISIBLE);
        }

        holder.rowContainer.setTag(current);

    }

    @Override
    public int getItemCount() {
        return passwords.size();
    }


    public boolean isTimeToUpdate(Date date){
    long tolerance = 5l;   //change this .............
    Date today=new Date();
    long difference_In_Time=today.getTime()-date.getTime();
    Log.d("diff",(TimeUnit
            .MILLISECONDS
            .toDays(difference_In_Time)
            % 365l)+"");
    return (TimeUnit
            .MILLISECONDS
            .toDays(difference_In_Time)
            % 365l)>tolerance;
    }
}
