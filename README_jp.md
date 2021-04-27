# Nearby Places for Android

<!-- MDTOC maxdepth:6 firsth1:0 numbering:0 flatten:0 bullets:1 updateOnSave:1 -->

- [機能](#機能)   
- [詳細なドキュメント](#詳細なドキュメント)   
- [開発手順](#開発手順)   
   - [リポジトリをフォークする](#リポジトリをフォークする)   
   - [リポジトリをクローンする](#リポジトリをクローンする)   
      - [コマンドラインでのGit](#コマンドラインでのGit)   
   - [フォークのためのリモートの設定](#フォークのためのリモートの設定)   
- [必要条件](#必要条件)   
- [リソース](#リソース)   
- [Issues](#issues)   
- [コントリビュート](#コントリビュート)   
- [MDTOC](#mdtoc)   
- [ライセンス](#ライセンス)   

<!-- /MDTOC -->
---

このリポジトリでは、[ArcGIS Runtime SDK for Android](https://developers.arcgis.com/android/) で構築された Android デバイス用の Nearby Places というサンプル アプリを提供しています。そのまま使用したり、多少のカスタマイズで、表示する場所の種類を変更することができます。

このアプリの詳細やRuntime機能の実装方法については、Esri の[**オープン ソース アプリ サイト**](https://developers.arcgis.com/example-apps/nearby-android/?utm_source=github&utm_medium=web&utm_campaign=example_apps_nearby_android) をご覧ください。[**GeoNetコミュニティ**](https://community.esri.com/community/developers/native-app-developers/arcgis-runtime-sdk-for-android)に参加することで、オープンソースアプリに関する最新の情報を入手できます。

## 機能

 * カテゴリを使ったジオコーディング
 * デバイスの位置情報の表示
 * 方位と距離の計算

## 詳細なドキュメント

アプリケーションのアーキテクチャや ArcGIS プラットフォームの活用方法など、アプリケーションの詳細な説明や、アプリケーションをすぐに使い始める方法については、[ドキュメント](./docs/README_ja.md)をご覧ください。

## 開発手順

この Nearby Places のリポジトリは、Android Studio のプロジェクトと App Module で構成されており、Android Studio に直接クローンをしてインポートすることができます。

### リポジトリをフォークする

[Nearby Places Android](https://github.com/Esri/nearby-android/fork) のリポジトリを**フォーク**します。

### リポジトリをクローンする

リポジトリをフォークすることで、クローンを作ることができます。

#### コマンドラインでのGit

1. Nearby Places のリポジトリを[クローン](https://help.github.com/articles/fork-a-repo#step-2-clone-your-fork)します。
2. `cd` コマンドで `nearby-android` のフォルダに移動します。
3. 変更を加えて[プルリクエスト](https://help.github.com/articles/creating-a-pull-request)を作成します。

### フォークのためのリモートの設定

フォークで変更を加え、その変更を上流のリポジトリと[同期](https://help.github.com/articles/syncing-a-fork/)させたい場合は、まず[リモートの設定](https://help.github.com/articles/configuring-a-remote-for-a-fork/)を行う必要があります。これは、ローカルブランチを作成した後、上流のブランチに[プルリクエスト](https://help.github.com/articles/creating-a-pull-request)を行う際に必要となります。

1. ターミナル (Mac ユーザの場合) あるいはコマンドプロンプト (Windows および Linux ユーザの場合) で `git remote -v` と実行すると、現在設定されている自分がフォークしたリモートリポジトリが表示されます。
2. `git remote add upstream https://github.com/Esri/nearby-android.git` を実行すると、フォークと同期する新しいリモートアップストリームリポジトリを指定できます。新しいアップストリームを確認するには `git remote -v` を実行します。

元のリポジトリに変更があった場合、フォークを同期して上流のリポジトリを最新の状態にすることができます。

1. ターミナルで、現在の作業ディレクトリをローカルプロジェクトに変更します。
2. `git fetch upstream` と入力して、上流のリポジトリからコミットを取得します。
3. `git checkout master` と入力して、フォークのローカルマスターブランチをチェックアウトします
4. `git merge upstream/master` で、ローカルの `master` ブランチを `upstream/master` に同期させます。**注意**: ローカルでの変更は保持され、フォークした master ブランチが上流のリポジトリと同期します。

## 必要条件

* [JDK 6 or higher](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [Android Studio](http://developer.android.com/sdk/index.html)

## リソース

* [Nearby Places](nearby-app/README.md)
* [ArcGIS Runtime SDK for Android Developers Site](https://developers.arcgis.com/android/)
* [ArcGIS Mobile Blog](http://blogs.esri.com/esri/arcgis/category/mobile/)
* [ArcGIS Developer Blog](http://blogs.esri.com/esri/arcgis/category/developer/)
* [Google+](https://plus.google.com/+esri/posts)
* [twitter@ArcGISRuntime](https://twitter.com/ArcGISRuntime)
* [twitter@esri](http://twitter.com/esri)

## Issues

バグの発見や、新機能の強化をリクエストしたい場合は、issue を提出してお知らせください。

## Contributing

どなたでも投稿を歓迎いたします。 プルリクエストは米国 Esri 社のリポジトリにて受け付けています。

1. 参加する
2. 問題点の報告
3. コードの投稿
4. ドキュメントの改善

## MDTOC

このリポジトリ内のドキュメントの目次の生成は、[Atom の MDTOC パッケージ](https://atom.io/packages/atom-mdtoc)を使用して作成されています。

## Licensing

Copyright 2017 Esri

Apache License, Version 2.0 (以下、「ライセンス」)の下でライセンスされており、ライセンスに準拠しない限り、このファイルを使用することはできません。ライセンスのコピーは以下の場所で入手できます。

http://www.apache.org/licenses/LICENSE-2.0

本契約に基づいて配布されるソフトウェアは、適用法で義務付けられている場合や書面で合意されている場合を除き、明示的または黙示的を問わず、いかなる種類の保証も条件もなしに、「そのまま」の状態で配布されます。本契約に基づく許可と制限を規定する特定の言語については、本契約を参照してください。

ライセンスのコピーは、リポジトリの [LICENSE](LICENSE) ファイルにあります。

デプロイしたアプリのライセンスについては、「アプリのライセンス(https://developers.arcgis.com/android/guide/license-your-app.htm)」をご覧ください。

[](Esri Tags: ArcGIS Android Mobile)
[](Esri Language: Java)​
