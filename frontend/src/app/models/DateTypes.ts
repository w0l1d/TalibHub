interface Month {
  name: string;
  value: number;
}

// declare a months data structure
const months:Month[] = [
  {name: 'January', value: 1},
  {name: 'February', value: 2},
  {name: 'March', value: 3},
  {name: 'April', value: 4},
  {name: 'May', value: 5},
  {name: 'June', value: 6},
  {name: 'July', value: 7},
  {name: 'August', value: 8},
  {name: 'September', value: 9},
  {name: 'October', value: 10},
  {name: 'November', value: 11},
  {name: 'December', value: 12}
];

// declare a years data structure dynamically 10 years from now and 100 years ago
const startYears:number[] = [];
const endYears:number[] = [];
const currentYear = new Date().getFullYear();
for (let i = currentYear - 100; i <= currentYear; i++) {
  startYears.push(i);
  endYears.push(i);
}
// add 10 years to the endYears array
for (let i = currentYear + 1; i <= currentYear + 10; i++) {
  endYears.push(i);
}
// invert the years array
startYears.reverse();
endYears.reverse();

export {Month, months, startYears, endYears};

