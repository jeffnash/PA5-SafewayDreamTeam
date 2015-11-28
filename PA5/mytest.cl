class Main inherits IO {
  a : Bool <- true;
  b : String <- "bbb";
  c : Int <- 188;
  main(): Object {
    {1 = 2; (new Cat).d(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);}
  };
};


class Dog {
	a : Int <- 100;
	b : String <- "Bool";
  what : Offset;
  e() : Object { a <- 150 };
  f() : Int { 30 };
};

class Cat inherits Dog {
	c : Bool <- true;
	d(a1 : Int, a2 : Int, a3: Int, a4: Int, a5:Int, a6:Int, a7:Int, a8:Int, a9:Int, a10:Int) : String { {a1;a2;a3;a4;a5;a6;a7;a8;a9;a10;"aaa";} };
  e() : Object { a <- 200 };
};

class Offset {
  ifthenelse() : Int { if true then 18 else 388 fi };
  equal() : Bool { 10 = 20 };
  whileloop() : Object { while false loop false pool };
  casestatement() : Object { case 3 of a : Int => a + 3; b : Bool => not b; esac };
};