cmake_minimum_required(VERSION 3.9)
project(Lab_1_bis)

set(CMAKE_C_STANDARD 99)
set(CMAKE_USE_PTHREADS_INIT true)
set(CMAKE_USE_PTHREADS_INIT ON)

find_package(Threads REQUIRED)
add_executable(Lab_1_bis
        main.c
        prod_cons.h)
target_link_libraries(Lab_1_bis ${CMAKE_THREAD_LIBS_INIT})