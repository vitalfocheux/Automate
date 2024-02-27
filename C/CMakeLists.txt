# How to use:
#   mkdir build
#   cd build
#   cmake ..
#   make
#
cmake_minimum_required(VERSION 3.10)

project(Automate
  LANGUAGES CXX C
)

find_package(Threads)


add_executable(testAutomate
  testAutomate.cc
  Automate.c
  googletest/googletest/src/gtest-all.cc
)

add_executable(testVector
  vector/testVector.cc
  vector/vector.c
  googletest/googletest/src/gtest-all.cc
)

add_executable(testSet
  set/testSet.cc
  set/set.c
  googletest/googletest/src/gtest-all.cc
)

add_executable(testMap
  map/testMap.cc
  map/map.c
  googletest/googletest/src/gtest-all.cc
)

add_executable(testPair
  pair/testPair.cc
  pair/pair.c
  googletest/googletest/src/gtest-all.cc
)

target_include_directories(testAutomate
  PRIVATE
    "${CMAKE_CURRENT_SOURCE_DIR}/googletest/googletest/include"
    "${CMAKE_CURRENT_SOURCE_DIR}/googletest/googletest"
)

target_include_directories(testVector
  PRIVATE
    "${CMAKE_CURRENT_SOURCE_DIR}/googletest/googletest/include"
    "${CMAKE_CURRENT_SOURCE_DIR}/googletest/googletest"
)

target_include_directories(testSet
  PRIVATE
    "${CMAKE_CURRENT_SOURCE_DIR}/googletest/googletest/include"
    "${CMAKE_CURRENT_SOURCE_DIR}/googletest/googletest"
)

target_include_directories(testMap
  PRIVATE
    "${CMAKE_CURRENT_SOURCE_DIR}/googletest/googletest/include"
    "${CMAKE_CURRENT_SOURCE_DIR}/googletest/googletest"
)

target_include_directories(testPair
  PRIVATE
    "${CMAKE_CURRENT_SOURCE_DIR}/googletest/googletest/include"
    "${CMAKE_CURRENT_SOURCE_DIR}/googletest/googletest"
)

target_link_libraries(testAutomate
  PRIVATE
    Threads::Threads
)

target_link_libraries(testVector
  PRIVATE
    Threads::Threads
)

target_link_libraries(testSet
  PRIVATE
    Threads::Threads
)

target_link_libraries(testMap
  PRIVATE
    Threads::Threads
)

target_link_libraries(testPair
  PRIVATE
    Threads::Threads
)

target_compile_options(testAutomate
  PRIVATE
    "-Wall" "-Wextra" "-pedantic" "-g" "-O2"
)

target_compile_options(testVector
  PRIVATE
    "-Wall" "-Wextra" "-pedantic" "-g" "-O2"
)

target_compile_options(testSet
  PRIVATE
    "-Wall" "-Wextra" "-pedantic" "-g" "-O2"
)

target_compile_options(testMap
  PRIVATE
    "-Wall" "-Wextra" "-pedantic" "-g" "-O2"
)

target_compile_options(testPair
  PRIVATE
    "-Wall" "-Wextra" "-pedantic" "-g" "-O2"
)

set_target_properties(testAutomate
  PROPERTIES
    CXX_STANDARD 17
    CXX_EXTENSIONS OFF
)

set_target_properties(testVector
  PROPERTIES
    CXX_STANDARD 17
    CXX_EXTENSIONS OFF
)

set_target_properties(testSet
  PROPERTIES
    CXX_STANDARD 17
    CXX_EXTENSIONS OFF
)

set_target_properties(testMap
  PROPERTIES
    CXX_STANDARD 17
    CXX_EXTENSIONS OFF
)


set_target_properties(testPair
  PROPERTIES
    CXX_STANDARD 17
    CXX_EXTENSIONS OFF
)
