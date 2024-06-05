c:
	gcc -o tester tester.c && ./tester

go:
	go run go/tester.go

rust:
	cargo run

java:
	java Tester.java

python:
	python3 tester.py

clean:
	rm -rf tester target *.class

.PHONY: c go rust java python clean
