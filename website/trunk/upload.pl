#!/usr/bin/perl -w

use strict;
use File::Basename;
use Getopt::Long;

use vars qw($COMMAND $opt_help $opt_dry_run $opt_user);

$COMMAND = basename($0);

sub usage
{
  print STDERR <<USAGE;
Usage is: $COMMAND: [OPTIONS]
    -n, --dry-run       Do not actually upload, but show what would be
                        uploaded.
    -h, --help          Help.  Display usage and options.
    -u, --user=USER     Set remote user to USER.
USAGE
}

my $dry_run_option;
my $rc;
my $command;
my $share_dir;

$opt_user = "";
$rc = GetOptions("dry-run|n",
                 "help|h",
                 "user|u=s");

if ($opt_help)
{
    usage;
    exit 0;
}

if (@ARGV != 0)
{
    usage;
    exit 1;
}

if ($opt_dry_run)
{
    $dry_run_option = "-n";
}
else
{
    $dry_run_option = "";
}

# Append an "@" sign to the user
if ($opt_user ne "")
{
    $opt_user .= "@"
}

my $remote_host = "dargo.crt.realtors.org";
my $remote_home = "/nar/www/sites/www.crt.realtors.org/www.crt.realtors.org/projects/rets/rex";
$command = 
    "rsync ${dry_run_option} -v -rltz -e \"ssh -x\" --delete " .
    "--exclude='*~' --exclude='CVS' --exclude='*.jar' " .
    "--exclude='/documentation/api/' " .
    "--exclude='/files/' " .
    "docroot/ ${opt_user}${remote_host}:${remote_home}/";

print "${command}\n\n" if ($opt_dry_run);

system "$command";
