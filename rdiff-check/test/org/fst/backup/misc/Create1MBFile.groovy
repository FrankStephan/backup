package org.fst.backup.misc

File file = new File('1Mb.txt')
if (file.exists()) {
	file.text = ''
}

int n = 2 ** 20

for (int i=0; i<n ; i++) {
	file.append('0')
}


