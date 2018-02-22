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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ListFragment extends Fragment {
  private static final String TAG = "Surface3D_ListFragment";
  private static int list_position = 0;
  private ListView mList;
  
  public static ListFragment newInstance() {
    ListFragment fragment = new ListFragment();
    return fragment;
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    Log.d(TAG, "Create view of ListFragment");
    View rootView = inflater.inflate(R.layout.fragment_layout, container, false);
    
    MainActivity activity = (MainActivity) getActivity();
    activity.setPrevFragmentType(FragmentType.FOLDERS);
    
    ImageStringAdapter asset_folder_adapter = new ImageStringAdapter(getActivity().getApplicationContext());
    asset_folder_adapter.add(getResources().getStringArray(R.array.models_folders));
    
    mList = (ListView) rootView.findViewById(R.id.assets_listview);
    mList.setAdapter(asset_folder_adapter);
    mList.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String typeModelName = (String) parent.getItemAtPosition(position);
        MainActivity activity = (MainActivity) getActivity();
        activity.setListButtonFragment(typeModelName, getFormatIdByPosition(position));
        activity.setAssetFolder(typeModelName);
        list_position = mList.getFirstVisiblePosition();
      }});
    
    View view = mList.getChildAt(0);
    int selected_child = (view == null) ? 0 : (view.getTop() - mList.getPaddingTop());
    mList.setSelectionFromTop(list_position, selected_child);
    return rootView;
  }
  
  /* Private methods */
  // --------------------------------------------------------------------------
  private int getFormatIdByPosition(int position) {
    int formatId = -1;
    switch (position) {
//      case 0:
//        formatId = R.string.model_folder_3D;
//        break;
      case 0://1:
        formatId = R.string.model_folder_3DS;
        break;
      case 1://2:
        formatId = R.string.model_folder_AC;
        break;
//      case 3:
//        formatId = R.string.model_folder_ASE;
//        break;
//      case 4:
//        formatId = R.string.model_folder_B3D;
//        break;
      case 2://5:
        formatId = R.string.model_folder_BLEND;
        break;
//      case 6:
//        formatId = R.string.model_folder_BVH;
//        break;
      case 3://7:
        formatId = R.string.model_folder_COB;
        break;
      case 4://8:
        formatId = R.string.model_folder_Collada;
        break;
//      case 9:
//        formatId = R.string.model_folder_CSM;
//        break;
      case 5://10:
        formatId = R.string.model_folder_DXF;
        break;
//      case 11:
//        formatId = R.string.model_folder_FurnitureOBJ;
//        break;
      case 6://12:
        formatId = R.string.model_folder_HMP;
        break;
//      case 13:
//        formatId = R.string.model_folder_IFC;
//        break;
//      case 14:
//        formatId = R.string.model_folder_IRR;
//        break;
//      case 15:
//        formatId = R.string.model_folder_IRRMesh;
//        break;
//      case 16:
//        formatId = R.string.model_folder_LWO;
//        break;
//      case 17:
//        formatId = R.string.model_folder_LWS;
//        break;
      case 7://18:
        formatId = R.string.model_folder_MD2;
        break;
      case 8://19:
        formatId = R.string.model_folder_MD5;
        break;
      case 9://20:
        formatId = R.string.model_folder_MDL;
        break;
      case 10://21:
        formatId = R.string.model_folder_MS3D;
        break;
      case 11://22:
        formatId = R.string.model_folder_NFF;
        break;
      case 12://23:
        formatId = R.string.model_folder_OBJ;
        break;
      case 13://24:
        formatId = R.string.model_folder_OFF;
        break;
//      case 25:
//        formatId = R.string.model_folder_Ogre;
//        break;
      case 14://26:
        formatId = R.string.model_folder_PLY;
        break;
      case 15://27:
        formatId = R.string.model_folder_Q3D;
        break;
      case 16://28:
        formatId = R.string.model_folder_RAW;
        break;
      case 17://29:
        formatId = R.string.model_folder_SMD;
        break;
      case 18://30:
        formatId = R.string.model_folder_STL;
        break;
//      case 31:
//        formatId = R.string.model_folder_TER;
//        break;
//      case 32:
//        formatId = R.string.model_folder_WRL;
//        break;
//      case 33:
//        formatId = R.string.model_folder_X;
//        break;
      case 19://34:
        formatId = R.string.model_folder_XGL;
        break;
      default:
        break;
    }
    return formatId;
  }
}
