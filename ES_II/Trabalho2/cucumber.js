module.exports = {
  default: {
    paths: ['feature/*.feature'],
    require: ['feature/support/**/*.js', 'feature/step_definitions/**/*.js'],
    format: ['progress', 'html:relatorios/cucumber-report.html'],
    publish: false
  }
};