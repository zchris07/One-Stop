# Iteration 4 Retrospective

## Overview

In iteration 4, we further improved our beyond CRUD features including auto-text corrector and auto-scheduler. We also finished the majority of our hand-writing recognition feature and the feature is waited to be integrated into our deployed version. 

## Achievements

Built among previous achievements, in this iteration, we: 
  - Deployed the app both on Heroku and AWS. 
  - Refactored our code structure to achive modulization. We now have separate modules of controller and APIendpoint. 
  - finalized the calendar view to integrate the auto-scheduler. 
  - Added new feature for hand-writing recognition but the feature is waiting to be added into Heroku deployed version. 

## Difficulties We Had

We encountered a weired issue with Heroku Postgres interaction. For some reason the postgres on Heroku cannot support database table id column named other than "id". For example, for "usertable" if the id column is named "userid", the database will not work. We fixed the issue by only have data table id column named as "id". Furthermore, we discovered that the column name with mixed upper case letters and lower case letters will also cause problems, so we renamed each data table column name to avoid problems. 

## Reflections and Make-ups

N/A. 
