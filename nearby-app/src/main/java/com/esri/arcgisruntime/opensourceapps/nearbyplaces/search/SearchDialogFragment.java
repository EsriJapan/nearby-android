/* Copyright 2016 Esri
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * For additional information, contact:
 * Environmental Systems Research Institute, Inc.
 * Attn: Contracts Dept
 * 380 New York Street
 * Redlands, California, USA 92373
 *
 * email: contracts@esri.com
 *
 */
package com.esri.arcgisruntime.opensourceapps.nearbyplaces.search;

import android.Manifest;
import android.R.style;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.database.MatrixCursor;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.cursoradapter.widget.SimpleCursorAdapter;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.Multipoint;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.mapping.view.WrapAroundMode;
import com.esri.arcgisruntime.opensourceapps.nearbyplaces.R;
import com.esri.arcgisruntime.opensourceapps.nearbyplaces.R.id;
import com.esri.arcgisruntime.opensourceapps.nearbyplaces.R.layout;
import com.esri.arcgisruntime.opensourceapps.nearbyplaces.R.string;
import com.esri.arcgisruntime.opensourceapps.nearbyplaces.filter.FilterContract;
import com.esri.arcgisruntime.opensourceapps.nearbyplaces.map.MapContract;
import com.esri.arcgisruntime.opensourceapps.nearbyplaces.map.MapFragment;
import com.esri.arcgisruntime.opensourceapps.nearbyplaces.search.SearchContract.Presenter;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.tasks.geocode.GeocodeParameters;
import com.esri.arcgisruntime.tasks.geocode.GeocodeResult;
import com.esri.arcgisruntime.tasks.geocode.LocatorTask;
import com.esri.arcgisruntime.tasks.geocode.SuggestParameters;
import com.esri.arcgisruntime.tasks.geocode.SuggestResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Class representing the filter dialog used to set what types of places are
 * viewed in list or map.
 */
public class SearchDialogFragment extends DialogFragment implements SearchContract.View {
    private Presenter mPresenter = null;

    private SearchView mPoiSearchView;


  public SearchDialogFragment(){}

  @Override
  public final View onCreateView(final LayoutInflater inflater, final ViewGroup container,
      final Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    getDialog().setTitle(string.search_dialog);
    final View view = inflater.inflate(layout.search_view, container,false);
        // setup the two SearchViews and show text hint
        mPoiSearchView = view.findViewById(id.poi_searchView);
        mPoiSearchView.setIconified(true);
        mPoiSearchView.setFocusable(true);

        // setup redo search button
        Button redoSearchButton = view.findViewById(id.redo_search_button);
        // on redo button click call redoSearchInThisArea
//        redoSearchButton.setOnClickListener(v -> redoSearchInThisArea(inflater, container, savedInstanceState));
        redoSearchButton.setOnClickListener(new OnClickListener() {
            @Override public void onClick(final View v) {
                final Activity activity = getActivity();
                if (activity instanceof SearchContract.SearchView){
                    ((SearchContract.SearchView) activity).onSearchDialogClose(true, view);
                }
                dismiss();
            }
        });

//        setupPoi();
    return view;
  }

  @Override
  public final void onCreate(final Bundle savedBundleState){
    super.onCreate(savedBundleState);
    setStyle(DialogFragment.STYLE_NORMAL, style.Theme_Material_Light_Dialog);
    mPresenter.start();
  }

  @Override public final void setPresenter(final Presenter presenter) {
    mPresenter = presenter;
  }


  public class SearchItemAdapter extends ArrayAdapter<SearchItem>{
    public SearchItemAdapter(final Context context, final List<SearchItem> items){
      super(context,0, items);
    }

    private class ViewHolder {
      Button btn = null;
      TextView txtName = null;
    }
    @NonNull @Override
    public final View getView(final int position, View convertView, @NonNull final ViewGroup parent) {
      final SearchDialogFragment.SearchItemAdapter.ViewHolder holder;

      // Get the data item for this position
      final SearchItem item = getItem(position);
      // Check if an existing view is being reused, otherwise inflate the view
      if (convertView == null) {
        convertView = LayoutInflater.from(getActivity()).inflate(layout.filter_list_item, parent, false);
        holder = new SearchDialogFragment.SearchItemAdapter.ViewHolder();
        holder.btn = convertView.findViewById(id.categoryBtn);
        holder.txtName = convertView.findViewById(id.categoryName);
        convertView.setTag(holder);
      }else{
        holder = (SearchDialogFragment.SearchItemAdapter.ViewHolder) convertView.getTag();
      }
      // Lookup view for data population
      holder.txtName.setText((item != null) ? item.getTitle() : null);

      if (item.getSelected()) {
        holder.btn.setBackgroundResource(item.getSelectedIconId());
        holder.btn.setAlpha(1.0f);
      } else {
        holder.btn.setBackgroundResource(item.getIconId());
        holder.btn.setAlpha(0.5f);
      }

      // Attach listener that toggles selected state of category
      convertView.setOnClickListener(new OnClickListener() {
        @Override public void onClick(final View v) {
          final SearchItem clickedItem = getItem(position);
          if (clickedItem !=  null ){
            if (clickedItem.getSelected()){
              clickedItem.setSelected(false);
              holder.btn.setBackgroundResource(item.getIconId());
              holder.btn.setAlpha(0.5f);
            }else {
              clickedItem.setSelected(true);
              holder.btn.setBackgroundResource(item.getSelectedIconId());
              holder.btn.setAlpha(1.0f);
            }
          }

        }
      });

      // Return the completed view to render on screen
      return convertView;
    }
  }
}
