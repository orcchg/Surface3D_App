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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ListButtonFragment extends Fragment {
  private static final String TAG = "Surface3D_ListButtonFragment";
  private static final String bundleKey_Title = "bundleKey_Title";
  private static final String bundleKey_Format = "bundleKey_Format";
  private static int list_position = 0;
  
  private TextView mTitle;
  private Button mBackButton;
  private ListView mList;
  
  private String mTitleText = "";
  private int mModelFormatId;
  
  public static ListButtonFragment newInstance(
      String title,
      int formatId) {
    
    ListButtonFragment fragment = new ListButtonFragment();
    Bundle args = new Bundle();
    args.putString(bundleKey_Title, title);
    args.putInt(bundleKey_Format, formatId);
    fragment.setArguments(args);
    return fragment;
  }
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Bundle args = getArguments();
    mTitleText = args.getString(bundleKey_Title);
    mModelFormatId = args.getInt(bundleKey_Format);
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    Log.d(TAG, "Create view of ListButtonFragment");
    View rootView = inflater.inflate(R.layout.fragment_btn_layout, container, false);
    
    MainActivity activity = (MainActivity) getActivity();
    activity.setPrevFragmentType(FragmentType.MODELS);
    
    mTitle = (TextView) rootView.findViewById(R.id.folder_title);
    mTitle.setText(mTitleText);
    
    mBackButton = (Button) rootView.findViewById(R.id.select_asset_button);
    mBackButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        MainActivity activity = (MainActivity) getActivity();
        activity.setListFragment();
      }});
    
    StringAdapter asset_models_adapter = new StringAdapter(getActivity().getApplicationContext());
    asset_models_adapter.add(getResources().getStringArray(getArrayIdByFormatId(mModelFormatId)));

    mList = (ListView) rootView.findViewById(R.id.assets_listview);
    mList.setAdapter(asset_models_adapter);
    mList.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String resource = (String) parent.getItemAtPosition(position);
        MainActivity activity = (MainActivity) getActivity();
        activity.loadAsset(mTitleText, resource);
        list_position = mList.getFirstVisiblePosition();
      }});
    
    View view = mList.getChildAt(0);
    int selected_child = (view == null) ? 0 : (view.getTop() - mList.getPaddingTop());
    mList.setSelectionFromTop(list_position, selected_child);
    return rootView;
  }
  
  /* Private methods */
  // --------------------------------------------------------------------------
  private int getArrayIdByFormatId(int formatId) {
    int arrayId = -1;
    switch (formatId) {
      case R.string.model_folder_3D:
        arrayId = R.array.models_3D;
        break;
      case R.string.model_folder_3DS:
        arrayId = R.array.models_3DS;
        break;
      case R.string.model_folder_AC:
        arrayId = R.array.models_AC;
        break;
      case R.string.model_folder_ASE:
        arrayId = R.array.models_ASE;
        break;
      case R.string.model_folder_B3D:
        arrayId = R.array.models_B3D;
        break;
      case R.string.model_folder_BLEND:
        arrayId = R.array.models_BLEND;
        break;
      case R.string.model_folder_BVH:
        arrayId = R.array.models_BVH;
        break;
      case R.string.model_folder_COB:
        arrayId = R.array.models_COB;
        break;
      case R.string.model_folder_Collada:
        arrayId = R.array.models_Collada;
        break;
      case R.string.model_folder_CSM:
        arrayId = R.array.models_CSM;
        break;
      case R.string.model_folder_DXF:
        arrayId = R.array.models_DXF;
        break;
      case R.string.model_folder_FurnitureOBJ:
        arrayId = R.array.models_FurnitureOBJ;
        break;
      case R.string.model_folder_HMP:
        arrayId = R.array.models_HMP;
        break;
      case R.string.model_folder_IFC:
        arrayId = R.array.models_IFC;
        break;
      case R.string.model_folder_IRR:
        arrayId = R.array.models_IRR;
        break;
      case R.string.model_folder_IRRMesh:
        arrayId = R.array.models_IRRMesh;
        break;
      case R.string.model_folder_LWO:
        arrayId = R.array.models_LWO;
        break;
      case R.string.model_folder_LWS:
        arrayId = R.array.models_LWS;
        break;
      case R.string.model_folder_MD2:
        arrayId = R.array.models_MD2;
        break;
      case R.string.model_folder_MD5:
        arrayId = R.array.models_MD5;
        break;
      case R.string.model_folder_MDL:
        arrayId = R.array.models_MDL;
        break;
      case R.string.model_folder_MS3D:
        arrayId = R.array.models_MS3D;
        break;
      case R.string.model_folder_NFF:
        arrayId = R.array.models_NFF;
        break;
      case R.string.model_folder_OBJ:
        arrayId = R.array.models_OBJ;
        break;
      case R.string.model_folder_OFF:
        arrayId = R.array.models_OFF;
        break;
      case R.string.model_folder_Ogre:
        arrayId = R.array.models_Ogre;
        break;
      case R.string.model_folder_PLY:
        arrayId = R.array.models_PLY;
        break;
      case R.string.model_folder_Q3D:
        arrayId = R.array.models_Q3D;
        break;
      case R.string.model_folder_RAW:
        arrayId = R.array.models_RAW;
        break;
      case R.string.model_folder_SMD:
        arrayId = R.array.models_SMD;
        break;
      case R.string.model_folder_STL:
        arrayId = R.array.models_STL;
        break;
      case R.string.model_folder_TER:
        arrayId = R.array.models_TER;
        break;
      case R.string.model_folder_WRL:
        arrayId = R.array.models_WRL;
        break;
      case R.string.model_folder_X:
        arrayId = R.array.models_X;
        break;
      case R.string.model_folder_XGL:
        arrayId = R.array.models_XGL;
        break;
      default:
        break;
    }
    return arrayId;
  }
}
