package org.fst.backup.test.unit.ui


File root = new File('root')
root.mkdir()
FileTreeBuilder ftb = new FileTreeBuilder(root)
ftb {
	'a0.suf'('')
	a0 {
		a1 { 'a2.suf'('') }
		b1 { 'b2.suf'('') }
	}
	'b0.suf'('')
	'c0.suf'('')
}
