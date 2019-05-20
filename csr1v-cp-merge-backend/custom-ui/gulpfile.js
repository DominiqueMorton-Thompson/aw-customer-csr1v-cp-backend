var gulp = require('gulp'),
  uglify = require('gulp-uglify'),
  pump = require('pump'),
  gulpTS = require('gulp-typescript'),
  less = require('gulp-less'),
  props = require('gulp-props');
  path = require('path'),
  del = require('del'),
  rename = require('gulp-rename'),
  runSequence = require('run-sequence'),
  fs = require('fs'),
  sourcemaps = require('gulp-sourcemaps'),
  rootFolder = path.join(__dirname),
  srcFolder = path.join(rootFolder, 'custom-ui'),
  buildFolder = path.join(rootFolder, 'generated'),
  lessFolder = path.join(rootFolder, 'less'),
  distFolder = path.join(rootFolder, 'generated');
  packageLockJson = path.join(rootFolder, '/package-lock.json');

gulp.task('tsc', function () {  
  var tsProject = gulpTS.createProject("tsconfig.json");
  return tsProject.src()
    .pipe(tsProject())
    .pipe(gulp.dest(distFolder));
});

gulp.task('compress', function (cb) {
  return pump([
    gulp.src(`${rootFolder}/generated/**/*.js`),
    uglify(),
    gulp.dest(`${rootFolder}/generated`)
  ]
  );
});

gulp.task('copy:resources', function () {
  return gulp.src(`../resources/**/*`)
    .pipe(gulp.dest(`${rootFolder}/generated`));
});

gulp.task('copy:SolutionUIFiles', function () {
  const prop = require("./deploy.json");
  var dest = prop['ao.composite.server.path'].startsWith('.') ? '../' + prop['ao.composite.server.path'] + '/src/main/webapp/main-ui/solutions/' + prop["solution.name"] : prop['ao.composite.server.path'] + '/src/main/webapp/main-ui/solutions/' + prop["solution.name"]; 
  console.log(dest);
  return gulp.src(`${rootFolder}/generated/**/*`)
    .pipe(gulp.dest(`${dest}`));
});

gulp.task('less:build', function () {
  const prop = require("./build.json");
  const fileName = prop["less.file.name"];
  return gulp.src(`${lessFolder}/${fileName}.less`)
    .pipe(less())
    .pipe(rename(fileName + '.css'))
    .pipe(gulp.dest(`../resources/css/`));
});

gulp.task('less:watch', function () {
  gulp.watch(`${srcFolder}/**/*.less`, ['less']);
});

gulp.task('clean:package_lock_json', function () {
  console.log('deleting package-lock.json ...');
  return del(packageLockJson);
});

gulp.task('read:properties', function () {
  gulp.src('./build.properties')
    .pipe(props({ namespace: '', space: 2 }))
    .pipe(gulp.dest('./'));
});

gulp.task('build', function () {
  del(buildFolder).then((res) => {
    console.log('deleted ' + res);
    runSequence(
      'tsc',
      'less:build',      
      function (err) {
        if (err) {
          console.log('ERROR:', err.message);
          del(buildFolder);
        } else {
          console.log('Compilation finished succesfully');
        }
      });
  });
});
