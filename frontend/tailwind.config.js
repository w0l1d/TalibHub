/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {
      colors: {
        bodyLightWhite: "#F9F9F9",
        bodyLightGray: "#D9D9D9",
        bodyWhite: "#F3F3F3",
        bodyLightPurple: "#CCB3F5",
        bodyGray: "#9F9999",
        textBlack: "#323232",
      },
      fontSize: {
        ss: '0.65rem',
      }
  
    },
    screens: {
      '2xl': {'max': '1535px'},
      // => @media (max-width: 1535px) { ... }

      'xl': {'max': '1279px'},
      // => @media (max-width: 1279px) { ... }

      'lg': {'max': '1023px'},
      // => @media (max-width: 1023px) { ... }

      'md': {'max': '767px'},
      // => @media (max-width: 767px) { ... }

      'sm': {'max': '639px'},
      // => @media (max-width: 639px) { ... }
    }

  },
  plugins: [],
}

