/**
 * Copyright (c) 2015, Alov Maxim <alovmax@yandex.ru>
 * 
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials provided with
 * the distribution.
 * 
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.orcchg.surface3dapp;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class InfoFragment extends Fragment {
  private static final String TAG = "Surface3D_InfoFragment";
  private static final String bundleKey_Title = "bundleKey_Title";
  private static final String bundleKey_Name = "bundleKey_Name";
  private static final String bundleKey_Vertices = "bundleKey_Vertices";
  private static final String bundleKey_Polygons = "bundleKey_Polygons";
  private static final String bundleKey_Colors = "bundleKey_Colors";
  private static final String bundleKey_Textures = "bundleKey_Textures";
  private static final String bundleKey_Materials = "bundleKey_Materials";
  private static final String bundleKey_Meshes = "bundleKey_Meshes";
  private static int list_position = 0;
  
  private TextView mTitle;
  private Button mBackButton;
  private ListView mList;
  
  private String mTitleText = "";
  private String mModelName = "";
  private int mModelVertices;
  private int mModelPolygons;
  private int mModelColors;
  private int mModelTextures;
  private int mModelMaterials;
  private int mModelMeshes;

  public static InfoFragment newInstance(
      String title,
      String name,
      int vertices,
      int polygons,
      int colors,
      int textures,
      int materials,
      int meshes) {
    
    InfoFragment fragment = new InfoFragment();
    Bundle args = new Bundle();
    args.putString(bundleKey_Title, title);
    args.putString(bundleKey_Name, name);
    args.putInt(bundleKey_Vertices, vertices);
    args.putInt(bundleKey_Polygons, polygons);
    args.putInt(bundleKey_Colors, colors);
    args.putInt(bundleKey_Textures, textures);
    args.putInt(bundleKey_Materials, materials);
    args.putInt(bundleKey_Meshes, meshes);
    fragment.setArguments(args);
    return fragment;
  }
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Bundle args = getArguments();
    mTitleText = args.getString(bundleKey_Title);
    mModelName = args.getString(bundleKey_Name);
    mModelVertices = args.getInt(bundleKey_Vertices);
    mModelPolygons = args.getInt(bundleKey_Polygons);
    mModelColors = args.getInt(bundleKey_Colors);
    mModelTextures = args.getInt(bundleKey_Textures);
    mModelMaterials = args.getInt(bundleKey_Materials);
    mModelMeshes = args.getInt(bundleKey_Meshes);
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    Log.d(TAG, "Create view of InfoFragment");
    View rootView = inflater.inflate(R.layout.info_fragment_layout, container, false);
    
    mTitle = (TextView) rootView.findViewById(R.id.assetinfo_title);
    mTitle.setText(mTitleText);
    
    mBackButton = (Button) rootView.findViewById(R.id.back_info_button);
    mBackButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        MainActivity activity = (MainActivity) getActivity();
        switch (activity.getPrevFragmentType()) {
          case INFO:
          case FOLDERS:
            activity.setListFragment();
            break;
          case MODELS:
            String assetFolder = activity.getAssetFolder();
            activity.setListButtonFragment(assetFolder, activity.getFormatIdByString(assetFolder));
            break;
        }
      }});
    
    StringAdapter info_adapter = new StringAdapter(getActivity().getApplicationContext());
    info_adapter.add("Name: " + mModelName);
    info_adapter.add("Vertices: " + mModelVertices);
    info_adapter.add("Polygons: " + mModelPolygons);
    info_adapter.add("Colors: " + mModelColors);
    info_adapter.add("Textures: " + mModelTextures);
    info_adapter.add("Materials: " + mModelMaterials);
    info_adapter.add("Meshes: " + mModelMeshes);
    
    mList = (ListView) rootView.findViewById(R.id.assetinfo_listview);
    mList.setAdapter(info_adapter);
    
    View view = mList.getChildAt(0);
    int selected_child = (view == null) ? 0 : (view.getTop() - mList.getPaddingTop());
    mList.setSelectionFromTop(list_position, selected_child);
    return rootView;
  }
}
