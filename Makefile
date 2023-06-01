.PHONY: producer
producer:
	cd producer && mvn clean install

.PHONY: consumer
consumer:
	cd consumer && mvn clean install

all: producer consumer

.PHONY: clean
clean:
	cd producer && mvn clean
	cd consumer && mvn clean