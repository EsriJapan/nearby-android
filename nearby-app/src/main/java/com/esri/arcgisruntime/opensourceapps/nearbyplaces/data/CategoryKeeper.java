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
package com.esri.arcgisruntime.opensourceapps.nearbyplaces.data;

import com.esri.arcgisruntime.opensourceapps.nearbyplaces.R;
import com.esri.arcgisruntime.opensourceapps.nearbyplaces.filter.FilterItem;
import com.esri.arcgisruntime.opensourceapps.nearbyplaces.search.SearchItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper that maintains the categories of supported places.
 */
public class CategoryKeeper {
    private static CategoryKeeper instance = null;
    private final ArrayList<FilterItem> mCategories = new ArrayList<>();
    private final ArrayList<SearchItem> mSCategories = new ArrayList<>();

    // Localize Add
    private static final String Bar = "バー";
    private static final String CoffeeShop = "喫茶店";
    private static final String Food = "飲食店";
    private static final String Hotel = "ホテル";
    private static final String Pizza = "ピザ";

    private CategoryKeeper(){
      mCategories.add(new FilterItem(Bar, R.drawable.ic_local_bar_grey_48dp, true, R.drawable.ic_local_bar_blue_48dp, "Bar or Pub"));
      mCategories.add(new FilterItem(CoffeeShop, R.drawable.ic_local_cafe_grey_48dp, true, R.drawable.ic_local_cafe_blue_48dp, "Coffee Shop"));
      mCategories.add(new FilterItem(Food, R.drawable.ic_local_dining_grey_48dp, true, R.drawable.ic_local_dining_blue_48dp, "Food"));
      mCategories.add(new FilterItem(Hotel, R.drawable.ic_local_hotel_grey_48dp, true, R.drawable.ic_local_hotel_blue_48dp, "Hotel"));
      mCategories.add(new FilterItem(Pizza, R.drawable.ic_local_pizza_gray_48dp, false, R.drawable.ic_local_pizza_blue_48dp, "Pizza"));
    }

    public static CategoryKeeper getInstance(){
      if (CategoryKeeper.instance == null){
        CategoryKeeper.instance = new CategoryKeeper();
      }
      return CategoryKeeper.instance;
    }

    public final List<FilterItem> getCategories(){
      return Collections.unmodifiableList(mCategories);
    }

    public final List<SearchItem> getSCategories(){
        return Collections.unmodifiableList(mSCategories);
    }


    public final List<String> getSelectedTypes(){
        final List<String> selectedTypes = new ArrayList<>();
        for (final FilterItem item : mCategories){
            if (!item.getSelected()){
                // Because places with food are sub-categorized by
                // food type, add them to the filter list.
                if (item.getCategoryName().equalsIgnoreCase("Food")){
                    selectedTypes.addAll(CategoryHelper.foodTypes);
                }else {
                    selectedTypes.add(item.getCategoryName());
                }
            }
        }
        return selectedTypes;
    }

    private final Map<String, String> categoryMap = new HashMap<>();
    public final Map<String, String> getCategoryMap() {
        categoryMap.put("遊園地","Amusement Park");
        categoryMap.put("水族館","Aquarium");
        categoryMap.put("アートギャラリー","Art Gallery");
        categoryMap.put("美術館","Art Museum");
        categoryMap.put("ビリヤード","Billiards");
        categoryMap.put("ボウリング場","Bowling Alley");
        categoryMap.put("映画館","Cinema");
        categoryMap.put("ランドマーク","Landmark");
        categoryMap.put("博物館","Museum");
        categoryMap.put("科学博物館","Science Museum");
        categoryMap.put("観光名所","Tourist Attraction");
        categoryMap.put("動物園","Zoo");
        categoryMap.put("教育","Education");
        categoryMap.put("大学","College");
        categoryMap.put("学校","School");
        categoryMap.put("飲食店","Food");
        categoryMap.put("アフリカ料理","African Food");
        categoryMap.put("アメリカンフード","American Food");
        categoryMap.put("アルゼンチン料理","Argentinean Food");
        categoryMap.put("オーストラリア料理","Australian Food");
        categoryMap.put("オーストリア料理","Austrian Food");
        categoryMap.put("ベーカリー","Bakery");
        categoryMap.put("バーベキューと南部の食べ物","BBQ and Southern Food");
        categoryMap.put("ベルギー料理","Belgian Food");
        categoryMap.put("ビストロ","Bistro");
        categoryMap.put("ブラジル料理","Brazilian Food");
        categoryMap.put("朝食","Breakfast");
        categoryMap.put("ブリューパブ","Brewpub");
        categoryMap.put("イギリス諸島食品","British Isles Food");
        categoryMap.put("ハンバーガー","Burgers");
        categoryMap.put("ケイジャンとクレオールフード","Cajun and Creole Food");
        categoryMap.put("カリブ海料理","Caribbean Food");
        categoryMap.put("チキンレストラン","Chicken Restaurant");
        categoryMap.put("チリ料理","Chilean Food");
        categoryMap.put("中華料理","Chinese Food");
        categoryMap.put("コーヒーショップ","Coffee Shop");
        categoryMap.put("コンチネンタルフード","Continental Food");
        categoryMap.put("クレペリー","Creperie");
        categoryMap.put("東ヨーロッパ料理","East European Food");
        categoryMap.put("ファーストフード","Fast Food");
        categoryMap.put("フィリピン料理","Filipino Food");
        categoryMap.put("フランス料理","French Food");
        categoryMap.put("フュージョンフード","Fusion Food");
        categoryMap.put("ドイツ料理","German Food");
        categoryMap.put("ギリシャ料理","Greek Food");
        categoryMap.put("グリル","Grill");
        categoryMap.put("アイスクリームショップ","Ice Cream Shop");
        categoryMap.put("インド料理","Indian Food");
        categoryMap.put("インドネシア料理","Indonesian Food");
        categoryMap.put("インターナショナルフード","International Food");
        categoryMap.put("アイリッシュフード","Irish Food");
        categoryMap.put("イタリア料理","Italian Food");
        categoryMap.put("日本食","Japanese Food");
        categoryMap.put("和食","Japanese Food");
        categoryMap.put("韓国料理","Korean Food");
        categoryMap.put("ラテンアメリカ料理","Latin American Food");
        categoryMap.put("マレーシア料理","Malaysian Food");
        categoryMap.put("メキシコ料理","Mexican Food");
        categoryMap.put("中東料理","Middle Eastern Food");
        categoryMap.put("モロッコ料理","Moroccan Food");
        categoryMap.put("その他のレストラン","Other Restaurant");
        categoryMap.put("ペストリー","Pastries");
        categoryMap.put("ピザ","Pizza");
        categoryMap.put("ポーランド料理","Polish Food");
        categoryMap.put("ポルトガル料理","Portuguese Food");
        categoryMap.put("ロシア料理","Russian Food");
        categoryMap.put("サンドイッチショップ","Sandwich Shop");
        categoryMap.put("スカンジナビア料理","Scandinavian Food");
        categoryMap.put("シーフード","Seafood");
        categoryMap.put("スナック","Snacks");
        categoryMap.put("南米料理","South American Food");
        categoryMap.put("東南アジア料理","Southeast Asian Food");
        categoryMap.put("スペイン料理","Spanish Food");
        categoryMap.put("ステーキハウス","Steak House");
        categoryMap.put("寿司","Sushi");
        categoryMap.put("スイス料理","Swiss Food");
        categoryMap.put("タパス","Tapas");
        categoryMap.put("タイ料理","Thai Food");
        categoryMap.put("トルコ料理","Turkish Food");
        categoryMap.put("ベジタリアン料理","Vegetarian Food");
        categoryMap.put("ベトナム料理","Vietnamese Food");
        categoryMap.put("ワイナリー","Winery");
        categoryMap.put("バーまたはパブ","Bar or Pub");
        categoryMap.put("カラオケ","Karaoke");
        categoryMap.put("ナイトクラブ","Night Club");
        categoryMap.put("公園とアウトドア","Parks and Outdoors");
        categoryMap.put("アウトドア","Parks and Outdoors");
        categoryMap.put("ビーチ","Beach");
        categoryMap.put("キャンプ 場","Campground");
        categoryMap.put("自然保護区","Nature Reserve");
        categoryMap.put("その他の公園とアウトドア","Other Parks and Outdoors");
        categoryMap.put("公園","Park");
        categoryMap.put("競馬場","Racetrack");
        categoryMap.put("プール","Swimming Pool");
        categoryMap.put("テニスコート","Tennis Court");
        categoryMap.put("歯科医","Dentist");
        categoryMap.put("歯医者","Dentist");
        categoryMap.put("大使館","Embassy");
        categoryMap.put("工場","Factory");
        categoryMap.put("消防署","Fire Station");
        categoryMap.put("官公庁","Government Office");
        categoryMap.put("病院","Hospital");
        categoryMap.put("診療所","Medical Clinic");
        categoryMap.put("警察署","Police Station");
        categoryMap.put("郵便局","Post Office");
        categoryMap.put("発電所","Power Station");
        categoryMap.put("刑務所","Prison");
        categoryMap.put("公衆トイレ","Public Restroom");
        categoryMap.put("ラジオ局","Radio Station");
        categoryMap.put("神社","Shrine");
        categoryMap.put("寺院","Temple");
        categoryMap.put("寺","Temple");
        categoryMap.put("ATM","ATM");
        categoryMap.put("銀行","Bank");
        categoryMap.put("書店","Bookstore");
        categoryMap.put("コンビニ","Convenience Store");
        categoryMap.put("コンビニエンスストア","Convenience Store");
        categoryMap.put("デパート","Department Store");
        categoryMap.put("ガソリンスタンド","Gas Station");
        categoryMap.put("ホームセンター","Home Improvement Store");
        categoryMap.put("薬局","Pharmacy");
        categoryMap.put("ショッピングセンター","Shopping Center");
        categoryMap.put("空港","Airport");
        categoryMap.put("ホテル","Hotel");
        categoryMap.put("地下鉄駅","Metro Station");
        categoryMap.put("駅","Train Station");
        categoryMap.put("事業所","Business Facility");
        categoryMap.put("企業","Business Facility");
        categoryMap.put("会社","Business Facility");


        return categoryMap;
    }

    public final String getCate(String cate) {
        String rtnCate = "";
        for (String key : getCategoryMap().keySet()) {
            if (getCategoryMap().get(key).equalsIgnoreCase(cate)) {
                rtnCate = cate;
                break;
            }
        }
        return rtnCate;
    }
}
