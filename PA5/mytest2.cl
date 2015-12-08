class Main inherits IO {
	a : Animal;

	perform() : Object {
		case a of x : Pikachu => out_string(x.thunderbolt());
			y : Object => out_string("This shouldn't be printed in any case");
			z : Dog => out_string(z.role());
			animal : Cat => out_string(animal.meow());
			a : NoCat => out_string(a.no());
			j : Jaeseo => out_string(j.swipe_right());
			b : Animal => out_string("nothing to do");
			c : Bull => out_string(c.shit());
			d : Kitten => out_string(d.meow());
			e : Chihuahua => out_string(e.flirt());
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