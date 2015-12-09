
(*  Example cool program testing as many aspects of the code generator
    as possible.
 *)

class Main inherits IO {
	a : Animal;
	iteration : Int <- 0;

	perform() : Object {
		case a of
			y : Pikachu => out_int(let x : Int <- 2, y : Int <- 3, z : Int <- 4 in x + y + z - z * z / ~x);
			x : Dog => case x of 
				c : Chihuahua => out_string("bark");
				j : Jaeseo => out_string("hi");
				d : Dog => out_string(d.bark());
				esac;
			z : Cat => case z of
				k : Kitten => out_string(k.meow());
				n : NoCat => out_string(n.meow());
				c : Cat => out_string(c.meow());
				esac;
			a : Bull => out_string(a.shit());
			n : Object => out_string("I am nothing");
		esac
	};


	main() : Object {
		
		{
		if isvoid a then out_string("this is VOID!\n") else out_string("this isn't void but should be\n") fi;		
		a <- new Chihuahua; a.init(); perform(); out_int(a.attract()); out_string("\n");
		if isvoid a then out_string("this is VOID but shouldn't be!\n") else out_string("this isn't void\n") fi;
		a <- new Kitten; a.init(); perform(); out_int(a.attract()); out_string("\n");
		a <- new Bull; a.init(); perform(); out_int(a.attract()); out_string("\n");
		a <- new Pikachu; a.init(); perform(); out_int(a.attract()); out_string("\n");
		a <- new Jaeseo; a.init(); perform(); out_int(a.attract()); out_string("\n");
		a <- new NoCat; a.init(); perform(); out_int(a.attract()); out_string("\n");
		a <- new Cat; a.init(); perform(); out_int(a.attract()); out_string("\n");
		a <- new Dog; a.init(); perform(); out_int(a.attract()); out_string("\n");
		a <- new Animal; a.init(); perform(); out_int(a.attract()); out_string("\n");
		while iteration < 5 loop { out_string("testing while\n"); iteration <- iteration + 1;} pool;
		if (true = true) = not false then out_string("true conditional\n") else out_string("shouldn't be printed\n") fi;
		if 6 < iteration then out_string("shouldn't be printed\n") else out_string("false conditional\n") fi;
		if 6 <= iteration then out_string("shouldn't be printed\n") else out_string("false conditional\n") fi;
		let a : Chihuahua <- new Chihuahua in {out_string("dynamic: ") ; out_string(a.bark()); out_string("\n");};
		let a : Chihuahua <- new Chihuahua in {out_string("static: ") ; out_string(a@Dog.bark()); out_string("\n");};
		}
	};
};

class Animal {
	init() : Object { legs <- 4 };
	legs : Int;
	cuteness : Int <- 0;
	attract() : Int { cuteness };
};

class Pikachu inherits Animal {
	init() : Object { cuteness <- 40 };
	thunderbolt() : String { "pika~~~~~~~chu~~~~~" };
};

class Dog inherits Animal {
	init() : Object { cuteness <- 100 };
	bark() : String { "Woof" };
	role() : String { {cuteness <- 200; "rooollllleeee";} };
};

class Chihuahua inherits Dog {
	init() : Object { cuteness <- 1000 };
	bark() : String { "woof" };
	flirt() : String { {cuteness <- 2000; "wooowoooo";} };
};

class Jaeseo inherits Dog {
	init() : Object { cuteness <- 99999999 };
	bark() : String { "nononono" }; 
	swipe_right() : String { "please date me I'm desperate" };
};

class Cat inherits Animal {
	init() : Object { cuteness <- 800 };
	meow() : String { "meowwwww~~~" };
};

class NoCat inherits Cat {
	init() : Object { cuteness <- 3000 };
	meow() : String { "meoowowowowow" };
	no() : String { "nonononononono" };

};

class Kitten inherits Cat {
	init() : Object { cuteness <- 5000 };
	meow() : String { "*&^%$#@" };
};

class Bull inherits Animal {
	init() : Object { cuteness <- ~100 };
	shit() : String { "BS" };
};