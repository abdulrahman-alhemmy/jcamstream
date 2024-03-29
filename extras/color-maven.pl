#!/usr/bin/perl -w
#
# color-maven 
# 
# Perl script to give Maven nice colors for readabiliy.
#
# Papa Issa DIAKHATE <paissad@gmail.com>
#
# Example of use: mvn | ./color-maven
#                 mvn clean | ./color-maven
#
# You also can add that script to you $PATH and then do:
# Example: mvn | color-maven
#
# ===========================================================================

use Term::ANSIColor;
use strict;

$Term::ANSIColor::AUTORESET++;         # reset color after each print
$SIG{INT} = sub { print "\n"; exit; }; # reset color after Ctrl-C
$| = 1;                                # unbuffer STDOUT;

my %maven_rules = (
    'Scanning for projects...' => "bold blue",
    '^\[INFO\]'                => "bold",
    '^\[ERROR\]'               => "bold red",
    '^\[WARN(ING)?\]'          => "bold yellow",
    'FATAL.*'                  => "blink bold red",
    'DEBUG.*'                  => "clear",
    'Total time:'              => "green",
    'Finished at:'             => "green",
    'Final Memory:'            => "green",
    'BUILD SUCCESS'            => "bold green",
    'BUILD FAILURE'            => "bold red",
    '\[Help [0-9]\]'           => "cyan",
    '^ T E S T S'              => "bold blue",
    'Failures: [1-9]+(,|$)'    => "red",
    'Errors: [1-9]+(,|$)'      => "red",
    'Unknown lifecycle phase'  => "red",

);

#### Main loop
#

while (<>) {
    study;
    my $regex;
    foreach $regex (keys %maven_rules) {
        s/$regex/colored($&,$maven_rules{$regex})/ge;
    }
    print;
}

