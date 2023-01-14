const chalk = require("chalk");

const Console = {
  log: (s) => {
    console.log(s);
  },
  error: (s) => {
    console.error(chalk.red(s));
  },
  warn: (s) => {
    console.warn(chalk.yellow(s));
  },
  info: (s) => {
    console.info(chalk.blue(s));
  },
  debug: (s) => {
    console.debug(chalk.green(s));
  },
};

module.exports = Console;
