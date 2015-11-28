class Main inherits IO {
  a : Bool <- true;
  b : String <- "bbb";
  c : Int <- 188;
  main(): Object {
    {1 = 2; (new Cat)@Dog.e();}
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
	d(k : Int, l : Int) : String { {k;l;b;} };
  e() : Object { a <- 200 };
};

class Offset {
  ifthenelse() : Int { if true then 18 else 388 fi };
  equal() : Bool { 10 = 20 };
  whileloop() : Object { while false loop false pool };
  casestatement() : Object { case 3 of a : Int => a + 3; b : Bool => not b; esac };
};