w=open('/home/ubuntu/comma_save_file-1.txt','w')

with open('/home/ubuntu/Text_files/save/part-00000','r') as f:
	l=f.readlines()
	lst=list()
	print("len(l): ",len(l))
	for i in range(0,len(l)):
		s=l[i]
		if(s=="\n"):
			continue
		#print("s: ",s)
		ss=""
		xx=s.split()
		x=list()

		for j in range(0,len(xx)-1):
			if(xx[j]!='.' and xx[j]!=' ' and xx[j]!='@' and xx[j]!='-' and xx[j]!=','):
				r=xx[j]
				rr=""
				for k in range(0,len(r)):
					if(r[k]!='.' and r[k]!=',' and r[k]!='\"' and r[k]!='#' and r[k]!='@' and r[k]!=';' and r[k]!=':' and r[k]!='-' and r[k]!=',' and r[k]!="'"):
						rr+=r[k]
				#x[j]=rr
				x.append(rr)
				#print("rr: ",rr)
				#print("x[j]: ",x[j])
			else:
				del xx[j]	
		
		print("x: ",x)
		x.sort()
		
		if(len(x)==0):
			continue
		prev=""
		for j in range(0,len(x)-1):	
			if(x[j]!=prev):
				prev=x[j]
				ss+=x[j]+","

		if(x[len(x)-1]!=prev):		
			ss+=x[len(x)-1]
		else:
			ss=ss[0:len(ss)-1]			
		lst.append(ss+"\n")

	# ss=lst[len(lst)-1]
	# ss=ss[0:len(ss)-1]
	# lst[len(lst)-1]=ss
	print("lst: ",lst)	
	w.writelines(lst)	
