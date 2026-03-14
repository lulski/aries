# RAM

A modern, responsive personal website and blog built with Next.js, featuring rich text editing, authentication, and a clean UI powered by Mantine.

## Features

- **Personal Portfolio**: Showcase your profile, skills, and experience
- **Blog System**: Create, edit, and manage blog posts with rich text editing using TipTap
- **Authentication**: Secure login system with session management
- **Responsive Design**: Mobile-first design with Mantine components
- **Theme Support**: Customizable themes with light/dark mode toggle
- **Pagination**: Efficient post listing with pagination
- **Error Handling**: Comprehensive error banners and handling
- **Storybook Integration**: Component documentation and testing

## Tech Stack

- **Framework**: Next.js 15 with App Router
- **UI Library**: Mantine v8
- **Rich Text Editor**: TipTap with extensions
- **Authentication**: Iron Session
- **Styling**: CSS Modules, SCSS
- **Testing**: Jest, Vitest
- **Storybook**: Component development and documentation
- **TypeScript**: Full type safety
- **Motion**: Framer Motion for animations

## Installation

1. Clone the repository:

   ```bash
   git clone <repository-url>
   cd ram
   ```

2. Install dependencies:

   ```bash
   npm install
   ```

3. Set up environment variables:
   Create a `.env.local` file with necessary environment variables (e.g., API keys, session secrets).

4. Run the development server:

   ```bash
   npm run dev
   ```

5. Open [http://localhost:3000](http://localhost:3000) in your browser.

## Usage

### Development

- `npm run dev`: Start the development server with Turbopack
- `npm run build`: Build the application for production
- `npm run start`: Start the production server
- `npm run lint`: Run ESLint for code quality

### Testing

- `npm test`: Run Jest tests
- `npm run test:watch`: Run tests in watch mode

### Storybook

- `npm run storybook`: Start Storybook development server
- `npm run build-storybook`: Build Storybook for deployment

## Project Structure

```
src/
├── app/                    # Next.js App Router pages
│   ├── api/               # API routes
│   ├── components/        # Reusable components
│   ├── context/           # React contexts
│   ├── lib/               # Utility functions and API calls
│   ├── types/             # TypeScript type definitions
│   └── stories/           # Storybook stories
├── public/                # Static assets
└── types/                 # Global type definitions
```

## Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin feature/your-feature`
5. Submit a pull request

## License

This project is licensed under the MIT License
