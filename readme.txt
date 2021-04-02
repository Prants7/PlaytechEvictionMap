EvictionMap project made with Gradle
written by Tõnis Prants

main functionality is under src/main/java/implementation
extra modules that clean up after expired data are under src/main/java/dataCleaner

main tests are under src/test/java/implementation
extra tests for interior components are under src/test/java/extraTests

How to use:

The api class for interacting with EvictionMap is EvictionMapImplementation<Key,Value>
(EvictionMap<Key,Value> itself is an interface for it)
This class can be made into an object with 2 different constructors.
First of them has only one input and that is the amount of seconds after witch the data becomes inaccessible.
The second constructor also accepts an element of DataCleaner<Key> type. The project has an InstantExpiringValueCleaner
class of cleaner that will make sure that all expired values will be removed from memory in about second or two.
(WARNING! due to not having enough coding time, this cleaner module doesn't work great with key values that get added
at the same time. Each one of these expiring pairs will slow down the cleaning process. Due to this, situations where
there are alot of put calls at the same time and short expiration times, it will take time for the actual database to
scrub out those pesky expired key value pairs from memory, the map itself will still return null for expired values.)

In order to get more control over data cleaning its possible to write extra modules that implement the DataCleanerBase
class and can call either
    callForCleanUpOnKey(Key keyToCallOn) for checks on specific key entries
    callForFullCleanUp() for checking the entire memory for expired entries
In both cases only already expired key-value pairs will be deleted.