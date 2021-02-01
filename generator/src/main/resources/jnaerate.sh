#
# Copyright (c) Libly - Terl Tech Ltd  • 01/02/2021, 20:07 • libly.co, goterl.com
#
# This Source Code Form is subject to the terms of the Mozilla Public
# License, v2.0. If a copy of the MPL was not distributed with this
# file, you can obtain one at http://mozilla.org/MPL/2.0/.
#

output_folder="../"
package="co.libly.hydride"
jnaeratorPath="$1"

echo "Removing previously generated libs"
rm -r co
rm -r libhydrogen

echo "Cloning libhydrogen"
git clone "https://github.com/jedisct1/libhydrogen" --depth 1

echo "JNAerate into $output_folder$package"
cd libhydrogen
java -jar $jnaeratorPath -library hydrogen hydrogen.h -o $output_folder -v -mode Directory -runtime JNA -skipDeprecated -forceStringSignatures -dontCastConstants -direct -noPrimitiveArrays -limitComments -noComments -noStaticInit -package $package -f

