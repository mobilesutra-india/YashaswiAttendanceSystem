package seedcommando.com.yashaswi;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.FruitHolder>{

    Context context;
    List<Employee> list = new ArrayList<>();

    public EmployeeAdapter(Context context, List<Employee> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public FruitHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view  = LayoutInflater.from(context).inflate(R.layout.item_emp,parent,false);

        return new FruitHolder(view);
    }

    @Override
    public void onBindViewHolder(final FruitHolder holder, final int position) {

        final Employee fruits = list.get(position);

        holder.tv_name.setText(fruits.getName());
        holder.tv_price.setText(fruits.getPrice());

        holder.checkBox.setChecked(fruits.isSelected());
        holder.checkBox.setTag(list.get(position));

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = "";
                Employee fruits1 = (Employee)holder.checkBox.getTag();

                fruits1.setSelected(holder.checkBox.isChecked());

                list.get(position).setSelected(holder.checkBox.isChecked());

                for (int j=0; j<list.size();j++){

                    if (list.get(j).isSelected() == true){
                        data = data + "\n" + list.get(j).getName().toString() + "   " + list.get(j).getPrice().toString();
                    }
                }
                Toast.makeText(context, "Selected Fruits : \n " + data, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class FruitHolder extends RecyclerView.ViewHolder{

        TextView tv_name,tv_price;
        CheckBox checkBox;

        public FruitHolder(View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.tv_fruit_name);
            tv_price = itemView.findViewById(R.id.tv_fruit_price);
            checkBox = itemView.findViewById(R.id.checkBox_select);
        }
    }

    public List<Employee> getFruitsList(){
        return list;
    }
}

