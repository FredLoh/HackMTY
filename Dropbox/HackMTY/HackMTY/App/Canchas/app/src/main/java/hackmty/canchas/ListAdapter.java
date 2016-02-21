package hackmty.canchas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

/**
 * baumann
 * 2/21/16
 */
public class ListAdapter extends ArrayAdapter<Cancha>{


    List<Cancha> ArrayListCanchas;
    int Resource;
    Context context;
    LayoutInflater vi;



    public ListAdapter(Context context, int resource, List<Cancha> objects) {
        super(context, resource, objects);

        ArrayListCanchas = objects;
        Resource = resource;
        this.context = context;
        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if(convertView == null){
            convertView = vi.inflate(Resource,null);
            holder = new ViewHolder();

            holder.tvNombre = (TextView)convertView.findViewById(R.id.ltv_name);
            holder.tvCosto = (TextView)convertView.findViewById(R.id.ltv_price);
            holder.tvDist = (TextView)convertView.findViewById(R.id.ltv_dist);


            convertView.setTag(holder);
        } else {

            holder = (ViewHolder)convertView.getTag();
        }



//        Typeface osl = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),
//                "fonts/OpenSans-Light.ttf");



        holder.tvNombre.setText(ArrayListCanchas.get(position).getName());


        String distanceOutput = "";

        double dist = ArrayListCanchas.get(position).getDist();

        if( dist < 1000){

            distanceOutput = String.format( "%.0f", dist ) + " m";


        } else {

            distanceOutput = String.format( "%.1f", dist/1000 ) + " km";
        }


        holder.tvDist.setText( distanceOutput );
//        holder.tvNombre.setTypeface(osl);


        return convertView;
    }

    static class ViewHolder{

        public TextView tvNombre;
        public TextView tvCosto;
        public TextView tvDist;

    }
}
