#!/bin/bash
# -*- coding: utf-8 -*-

# This file is part of VoR ~ Voice Recognition (0.0.4 or above)
# distributed under the MIT license.
#
# Copyright (c) 2014
# Giulio Orrù <giulio.orru@gmail.com>,
# Dipartimento di Matematica e Informatica: Università degli Studi di Cagliari.
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
# 
# The above copyright notice and this permission notice shall be included in
# all copies or substantial portions of the Software.
# 
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
# THE SOFTWARE.
 


## VERY IMPORTANT TO REMEMBER:
## GRAM_FILE must be listed in the assets.lst file!

GRAMMAR_NAME='voicere'
GRAM_FILE="models/grammar/voicere.gram"
DIC_FILE='models/dict/cmu07a.dic'

#ENCODING_JSGF="UTF-8";
ENCODING_JSGF="";
#LOCALE_JSGF="en";
LOCALE_JSGF="";

## head: inizialize the file
echo   "#JSGF V1.0 ${ENCODING_JSGF} ${LOCALE_JSGF};"> "${GRAM_FILE}"
echo   ""                                           >> "${GRAM_FILE}"
echo   "grammar ${GRAMMAR_NAME};"                   >> "${GRAM_FILE}"
echo   ""                                           >> "${GRAM_FILE}"

## main: fill the file
#echo   "public <item> = digits | forecast;"   >> "${GRAM_FILE}"
echo   "pubblic <item> =  oh"   >> "${GRAM_FILE}"
echo   "    | zero"             >> "${GRAM_FILE}"
echo   "    | one"              >> "${GRAM_FILE}"
echo   "    | two"              >> "${GRAM_FILE}"
echo   "    | three"            >> "${GRAM_FILE}"
echo   "    | four"             >> "${GRAM_FILE}"
echo   "    | five"             >> "${GRAM_FILE}"
echo   "    | six"              >> "${GRAM_FILE}"
echo   "    | seven"            >> "${GRAM_FILE}"
echo   "    | eight"            >> "${GRAM_FILE}"
echo   "    | nine"             >> "${GRAM_FILE}"
echo   "    ;"                  >> "${GRAM_FILE}"

## tail: finalize the file
#echo   ""                                       >> "${GRAM_FILE}"
#echo   "public <${GRAMMAR_NAME}> = <digit>;"   >> "${GRAM_FILE}"

# ultimate: correct file permission and make md5sum
chmod 0777 "${GRAM_FILE}"
md5sum "${GRAM_FILE}" | colrm 33    > "${GRAM_FILE}.md5"
echo ""                             >> "${GRAM_FILE}.md5"
