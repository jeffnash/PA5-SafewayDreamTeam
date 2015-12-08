--complicated_io_1.cl
class Main inherits IO {
	main(): Object {
		out_int ({out_string(type_name()); out_string("\n"); 1;}
		)
	};
}; /*Likely cuase is type_name being implemented incorrectly or return value is corrupted. */


complicated_bool
class Main inherits IO {
	main(): Object {
		out_int(if not ((true = false) = true)
			then
			1
			else
			0
			fi
		)
	};
};

complicated_object had abort