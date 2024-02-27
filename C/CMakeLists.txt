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

target_include_directories(testAutomate
  PRIVATE
    "${CMAKE_CURRENT_SOURCE_DIR}/googletest/googletest/include"
    "${CMAKE_CURRENT_SOURCE_DIR}/googletest/googletest"
)

target_link_libraries(testAutomate
  PRIVATE
    Threads::Threads
)

target_compile_options(testAutomate
  PRIVATE
    "-Wall" "-Wextra" "-pedantic" "-g" "-O2"
)

set_target_properties(testAutomate
  PROPERTIES
    CXX_STANDARD 17
    CXX_EXTENSIONS OFF
)
