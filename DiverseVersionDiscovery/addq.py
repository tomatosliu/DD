#coding: utf-8
import types
import json
from mmseg import seg_txt
#for i in seg_txt("最配偶的更动是：张无忌最后没有选定自己的配偶。"):
   	#print i
#print di
#   print type(i)

punctuation = [',','，', '.', '。', '?', '？', '!', '！', 
	'<', '《', '>', '》', '(', '（', ')', '）', ':', '：'
	';', '；', '"', '“', '”','{', '｛', '}', '｝', '、']
fileRead = open("./stopwords.txt", "r")
filewrite = open("./stopwords_1.txt", "w")

while True :
	oneWord = fileRead.readline()
	#读到了文件的末尾
	if not oneWord:
		break
	oneWord=oneWord.strip('\n')
	oneWord = '"' + oneWord + '", \n';

	filewrite.write(oneWord)