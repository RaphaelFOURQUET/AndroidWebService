package fr.adaming.website.bean;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import fr.adaming.website.R;
import fr.adaming.website.data.Constante;

/**
 * Created by INTI-0332 on 05/07/2016.
 */
public class CityBeanAdapter extends BaseAdapter {

//    private LinearLayout layoutCellule;

    private LayoutInflater mInflater;
    private List<CityBean> cityList;

    public List<CityBean> getCityList() {
        return cityList;
    }

    public void setCityList(List<CityBean> cityList) {
        this.cityList = cityList;
    }

    public CityBeanAdapter(Context context, List<CityBean> cityList) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.cityList = cityList;
    }

    @Override
    public int getCount() {
        return cityList.size();
    }

    @Override
    public CityBean getItem(int position) {
        return cityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View rowView, ViewGroup parent) {
        //Recyclage via rowView
        if (rowView == null) {
            Log.w(Constante.TAG, "Creation cellule");
            //création
            rowView = mInflater.inflate(R.layout.city_cellule, null);
        } else {
            Log.w(Constante.TAG, "Recyclage cellule");
        }


//        this.layoutCellule = (LinearLayout) rowView.findViewById(R.id.celluleCityLayout);


        //Recuperer les textView
        TextView cityNameView = (TextView) rowView.findViewById(R.id.cityName);
        TextView cityCpView = (TextView) rowView.findViewById(R.id.cityCp);

        //on remplit avec l'objet voulu
        final CityBean city = getItem(position);

        cityNameView.setText(city.getVille());
        cityCpView.setText(city.getCp());

        rowView.findViewById(R.id.celluleCityLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constante.LOG_DEV_MODE) Log.w(Constante.TAG, "Click detecte !");
                cityList.remove(city);
                notifyDataSetChanged();
            }
        });


        return rowView;
    }

}
