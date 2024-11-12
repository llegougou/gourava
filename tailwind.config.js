const { createGlobPatternsForDependencies } = require('@nx/angular/tailwind');
const { join } = require('path');

/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    join(__dirname, 'src/**/!(*.stories|*.spec).{ts,html}'),
    ...createGlobPatternsForDependencies(__dirname),
  ],
  theme: {
    fontFamily: {
      sans: 'Inter var, ui-sans-serif, system-ui',
      serif: 'Inter var, ui-sans-serif, system-ui',
    },
    fontSize: {
      sm: '0.875rem',
      base: '1.3rem',
      xl: '1.55rem',
      '2xl': '1.563rem',
      '3xl': '1.953rem',
      '4xl': '2.441rem',
      '5xl': '3.052rem',
    },
    extend: {
      colors: {
        primary: '#00796B',
        primaryLight: '#089889',
        secondary: '#FF7043',
        secondaryLight: '#FF8B66',
        background: '#FFF3E0',
        backgroundAnti: '#DCC8AA',
        backgroundMid:'#EEDEC5',
        accent: '#FFD700',
        neutral: '#424242',
      },
    },
  },
  daisyui: {
    themes: [
      {
        fantasy: {
          primary: '#00796B',
          primaryLight: '#089889',
          secondary: '#FF7043',
          secondaryLight: '#FF8B66',
          background: '#FFF3E0',
          backgroundAnti: '#DCC8AA',
          accent: '#FFD700',
          neutral: '#424242',
        },
      },
    ],
    base: true,
    styled: true,
    utils: true,
    prefix: '',
    logs: true,
    themeRoot: ':root',
  },
  plugins: [require('@tailwindcss/typography'), require('daisyui')],
};
