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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageStringAdapter extends BaseAdapter {
  private Context mContext;
  private List<String> names;
  
  /* View holder */
  private static class ViewHolder {
    @SuppressWarnings("unused")
    private ImageView image;
    private TextView name;
  }
  
  public ImageStringAdapter(Context context) {
    mContext = context;
    names = new ArrayList<>();
  }
  
  public void add(String name) {
    names.add(name);
  }
  
  public void add(List<String> names) {
    this.names.addAll(names);
  }
  
  public void add(String[] names) {
    this.names.addAll(Arrays.asList(names));
  }
  
  @Override
  public int getCount() {
    return names.size();
  }

  @Override
  public Object getItem(int position) {
    return names.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder holder = null;
    View rowView = convertView;
    LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    
    if (rowView == null) {
      rowView = inflater.inflate(R.layout.asset_folder_row, null, true);
      holder = new ViewHolder();
      holder.image = (ImageView) rowView.findViewById(R.id.asset_pictogram);
      holder.name = (TextView) rowView.findViewById(R.id.asset_name);
      rowView.setTag(holder);
    } else {
      holder = (ViewHolder) rowView.getTag();
    }
    holder.name.setText(names.get(position));
    
    return rowView;
  }

  public void clear() {
    names.clear();
  }
}
