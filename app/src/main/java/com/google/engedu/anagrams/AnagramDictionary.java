/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private ArrayList<String> wordList;
    private HashSet<String> wordSet;
    private HashMap<String,ArrayList<String>> lettersToWord;
    private HashMap<Integer,ArrayList<String>> sizeToWord;
    private int wordLength = 3;

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        wordList = new ArrayList<>();
        wordSet = new HashSet<>();
        lettersToWord = new HashMap<>();
        sizeToWord = new HashMap<>();

        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);
            String sort = sortString(word);
            if(lettersToWord.containsKey(sort)){
                ArrayList<String> list = lettersToWord.get(sort);
                list.add(word);
            }
            else{
                ArrayList<String> list = new ArrayList<>();
                list.add(word);
                lettersToWord.put(sort,list);
            }
            int size = word.length();
            if(sizeToWord.containsKey(size)){
                ArrayList<String> list = sizeToWord.get(size);
                list.add(word);
            }
            else{
                ArrayList<String> list = new ArrayList<>();
                list.add(word);
                sizeToWord.put(size, list);
            }
        }
    }

    public String sortString(String word){
        char arr[] = word.toCharArray();
        Arrays.sort(arr);
        return new String(arr);
    }


    public boolean isGoodWord(String word, String base) {
        if(wordSet.contains(word) && !word.contains(base)) return true;
        else return false;
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();

        String sortedWord = sortString(targetWord);
        int len = wordList.size();
        for(int i=0; i<len; i++) {
            String dSortedWord = sortString(wordList.get(i));
            if(dSortedWord.equals(sortedWord)){
                result.add(wordList.get(i));
            }
        }

        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();

        for(char ch='a'; ch<='z'; ch++){
            String tmpWord = word + ch;
            String sortedWord = sortString(tmpWord);
            if(lettersToWord.containsKey(sortedWord)){
                for(String str: lettersToWord.get(sortedWord)){
                    if(isGoodWord(str, word)) result.add(str);
                }
            }
        }

        return result;
    }

    public String pickGoodStarterWord() {

        if(wordLength<MAX_WORD_LENGTH) wordLength++;

        while(true){
            int rindx = random.nextInt(sizeToWord.get(wordLength).size());
            String word = sizeToWord.get(wordLength).get(rindx);
            if(getAnagramsWithOneMoreLetter(word).size()>=MIN_NUM_ANAGRAMS){
                return word;
            }
        }
    }
}
