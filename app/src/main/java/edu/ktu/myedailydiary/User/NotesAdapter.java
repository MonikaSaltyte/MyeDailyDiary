package edu.ktu.myedailydiary.User;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.ktu.myedailydiary.EditActivity;
import edu.ktu.myedailydiary.NoteActivity;
import edu.ktu.myedailydiary.R;
import edu.ktu.myedailydiary.SecondActivity;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyHolder>
{

    List<Notedata> noteslist;
    private Context context;
    public  NotesAdapter(List<Notedata> noteslist, Context context)
    {
        this.context=context;
        this.noteslist=noteslist;
    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item,viewGroup,false);

        MyHolder myHolder=new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int position) {
        Notedata data = noteslist.get(position);
        myHolder.date.setText(data.getDate());
        myHolder.title.setText(data.getTitle());
        myHolder.desc.setText(data.getDesc());
        myHolder.position = position;
        myHolder.setTag(position);
    }

    @Override
    public int getItemCount() {
        return noteslist.size();
    }

    class  MyHolder extends RecyclerView.ViewHolder  {
        TextView date,title,desc;
        Integer position = -1;
        View rootView;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            this.rootView = itemView;
            date=itemView.findViewById(R.id.date);
            title=itemView.findViewById(R.id.title);
            desc=itemView.findViewById(R.id.desc);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Notedata listdata=noteslist.get(getAdapterPosition());
                    Intent i=new Intent(context, EditActivity.class);
                    i.putExtra("nId", listdata.nId);
                    i.putExtra("date",listdata.date);
                    i.putExtra("title",listdata.title);
                    i.putExtra("desc",listdata.desc);
                    context.startActivity(i);

                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int postion = Integer.valueOf(v.getTag().toString());
                    noteslist.remove(postion);
                    notifyDataSetChanged();
                    return true;
                }
            });


        }
        public void setTag(int position){
            rootView.setTag(position);
        }
    }
}

