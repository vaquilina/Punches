# Punches

A Java Swing app designed to make creating shorthand song charts easier for drummers.

**__currently in development__**

## Overview

**Punches** is a Java application that was created to address the cumbersome task of writing simple song charts for live gigs and recording sessions. It aims to make this process much faster and more intuitive than doing so manually--with the added benefit of producing a prettier and more organized result.

Songs are assigned basic attributes (title, time signature, tempo) and broken up into Parts. Each Part consists of a name, length, position, notes and optionally a sheet music or tabulature snippet. The snippet is generated from a rhythm that is keyed in and rendered internally.

The intended purpose of the sheet/tab snippet is to provide a snapshot of the "essence" of the part. This could be the groove played during that part, or perhaps the "punches" in the section.

## Features

- Import/export Song from file (`.pnc`)
- Customizable Part cells
- Add "punches" to a Part by keying in a Rhythm (via keyboard) and converting it to a tabulature snippet, or sheet music snippet via [Lilypond](https://lilypond.org/)
  - Playback the rhythm you've just keyed in
- Write expressive notes for Parts using Markdown/basic HTML
- Generate neat Song charts and export as PDF

## Goals

- Re-order parts using drag-and-drop
- Import existing sheet or tabulature snippet
- Duplicate/copy Parts
- Intuitive undo/redo
- Companion mobile app (Android & iOS)
- Allow use of real MIDI instrument to key in rhythms
- Allow for more complex rhythm input 
- A lightweight editor for keyed-in rhythms

## Credits

- music programming API: [JFugue](http://www.jfugue.org)
- toolbar icons: [famfamfam](http://www.famfamfam.com/lab/icons/)
- markdown processor: [txtmark](https://github.com/rjeschke/txtmark) 
- PDF renderer: [openhtmltopdf](https://github.com/danfickle/openhtmltopdf)
- super-powered layout manager: [MigLayout](https://miglayout.com/)
- Swing utility framework: [SoftSmithy Swing Library](https://www.softsmithy.org/)
- GNU music engraver: [LilyPond](https://lilypond.org/)


