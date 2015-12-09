class Main inherits IO {
	a : Animal;

	perform() : Object {
		case a of
			y : Pikachu => out_int(let x : Int <- 2, y : Int <- 3, z : Int <- 4 in x + y + z - z * z / ~x);
			x : Dog => case x of 
				c: Chihuahua => out_string("bark");
				j: Jaeseo => out_string("hi");
				esac;
			z : Cat => case z of
				k : Kitten => out_string(k.meow());
				n : NoCat => out_string(n.meow());
				esac;
			a : Bull => out_string(a.shit());
		esac
	};


	main() : Object {
		{
		a <- new Chihuahua; a.init(); perform(); out_int(a.attract()); out_string("\n");
		a <- new Kitten; a.init(); perform(); out_int(a.attract()); out_string("\n");
		a <- new Bull; a.init(); perform(); out_int(a.attract()); out_string("\n");
		a <- new Pikachu; a.init(); perform(); out_int(a.attract()); out_string("\n");
		a <- new Jaeseo; a.init(); perform(); out_int(a.attract()); out_string("\n");
		a <- new NoCat; a.init(); perform(); out_int(a.attract()); out_string("\n");
		a <- new Cat; a.init(); perform(); out_int(a.attract()); out_string("\n");
		a <- new Dog; a.init(); perform(); out_int(a.attract()); out_string("\n");
		a <- new Animal; a.init(); perform(); out_int(a.attract()); out_string("\n");
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