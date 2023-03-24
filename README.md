# Delete-Duplications
OneDriveによって二重に保存されたファイルを削除する。
二重に保存されたファイルはファイル名の末尾にパソコンの名前がつく。
このプログラムを実行すると、result.txtとdelete.batが生成されます。
delete.batを実行すると重複ファイルを削除します。ゴミ箱へ移動ではなく、完全に削除されます。
result.batはファイル名が同じでも内容が異なる場合、アクセスできなかった場合のファイルのフルパスが出力されます。

コンパイルコマンド(サンプル)
cd "d:\Documents\Github\Delete-Duplications\Java\main\" ; if ($?) { javac -encoding UTF-8 Main.java Library.java } ; if ($?) { cd .. ; java main.Main }