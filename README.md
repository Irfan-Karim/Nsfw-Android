# NSFW - Not Safe For Work Detector Android

### This Android Library uses a Tensor Flow lite model to classify if the provided Image is Safe for work or not.

## Import

### Add it in your root build.gradle at the end of repositories:

```
allprojects {
  repositories {
  ...
  maven { url 'https://jitpack.io' }
  }
}
```

### Add the dependency

```
dependencies {
  implementation 'com.github.Irfan-Karim:Nsfw-Android:1.0.0'
}
```

## How to Use

### Initialze the instance of Analyzer

```
val analyzer = Analyzer(context)
```

### Provide the bitmap to be classified to analyzer's classify method

```
analyzer.classify(bitmap) { result ->
  binding.tvResult.text = result.name
}
```

### Output

Analyzer detects the following Five Categories

```
DRAWINGS
HENTAI
NEUTRAL
PORN
SEXY
```


