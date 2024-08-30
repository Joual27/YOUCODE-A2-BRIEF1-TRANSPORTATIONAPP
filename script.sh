#!/bin/bash

#Define paths and filenames
JAVAC=javac
JAVA=java
SRC_DIR=src/main/java
OUT_DIR=out
LIB_DIR=libs
LIBRARIES="$LIB_DIR/*"
MAIN_CLASS=com.youcode.transportationApp.Main

#Function to compile all Java source files
compile() {
    echo "Compiling Java source files in $SRC_DIR..."

    # Ensure the output directory exists
    mkdir -p $OUT_DIR

    # Compile all Java source files, include libraries in the classpath
    $JAVAC -cp "$LIBRARIES" -d $OUT_DIR $(find $SRC_DIR -name "*.java")
    if [ $? -ne 0 ]; then
        echo "Compilation failed."
        exit 1
    fi

    echo "Compilation successful."
}

#Function to run the compiled Java application
run() {
    compile  # Ensure compile is run before execution

    if [[ "$OSTYPE" == "linux-gnu"* ||  "$OSTYPE" == "darwin"* ]]; then
        CLASSPATH="$LIBRARIES:$OUT_DIR"
    elif [[ "$OSTYPE" == "cygwin" || "$OSTYPE" == "msys" || "$OSTYPE" == "win32" ]]; then
        CLASSPATH="$LIBRARIES;$OUT_DIR"
    else
        echo "Unsupported OS type: $OSTYPE"
        exit 1
    fi

    echo "Running $MAIN_CLASS with classpath $CLASSPATH..."
    $JAVA -cp "$CLASSPATH" $MAIN_CLASS
    if [ $? -ne 0 ]; then
        echo "Error: Failed to run $MAIN_CLASS. Check the classpath and main class name."
        exit 1
    fi
}

#Function to clean up generated class files
clean() {
    echo "Cleaning up..."
    rm -rf $OUT_DIR/*.class
    echo "Clean up successful."
}

#Parse command line arguments to determine which function to execute
case $1 in
    compile)
        clean
        compile
        ;;
    run)
        run
        ;;
    clean)
        clean
        ;;
    *)
        echo "Usage: $0 {compile|run|clean}"
        exit 1
        ;;
esac