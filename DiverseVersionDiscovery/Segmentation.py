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
fileRead = open("./MH370.txt", "r")
filewrite = open("./MH370Seg.txt", "w")

while True :
	oneWeibo = fileRead.readline()
	#读到了文件的末尾
	if not oneWeibo:
		break
	#开始进行分词: 组织成dic，key<word>:value<times>
	dic = {};
	for i in seg_txt(oneWeibo):
		#查看每一个词是否存在，然后，插入dic
		if i in dic:
			dic[i] = dic[i]+1
		else:
			dic.setdefault(i, 1);
	#输出dict
	for key in dic:
		if key not in punctuation:#标点符号去掉
			filewrite.write(key+' '+json.dumps(dic[key])+'\n')
	filewrite.write('#\n')