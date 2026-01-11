/** @type {import('tailwindcss').Config} */
module.exports = {
  darkMode: 'class', // Enable manual dark mode toggling
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {},
  },
  plugins: [
    require('daisyui'),
  ],
  daisyui: {
    themes: ["light", "dark", "forest", "cyberpunk", "retro"], // Add themes as needed
  },
}
