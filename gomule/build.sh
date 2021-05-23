ant Jar-BuildAllOptimezed
rm -rf GoMule *.zip
mkdir GoMule
cp -r d2111/ dupelists/ resources/ COPYING.txt GoMule.jar LICENSE.txt standard.css standard.dat GoMule
zip -r "GoMuleR4.3.$1_1.13.zip" GoMule