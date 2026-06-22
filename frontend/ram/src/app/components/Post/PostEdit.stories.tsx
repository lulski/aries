import { useForm } from "@mantine/form";
import type { Meta, StoryObj } from "@storybook/nextjs-vite";
import PostEdit from "./PostEdit";

const meta = {
  title: "Components/PostEdit",

  component: PostEdit,
  parameters: {
    layout: "fullscreen",
  },
} satisfies Meta<typeof PostEdit>;

export default meta;
type Story = StoryObj<typeof meta>;

type PostEditWrapperProps = {
  initialValues?: {
    title: string;
    content: string;
    id: string;
  };
  error?: string | null;
};

function PostEditWrapper({ initialValues, error }: PostEditWrapperProps) {
  const form = useForm({
    mode: "controlled",
    initialValues: initialValues || {
      title: "",
      content: "",
      id: "",
    },
    validate: {
      title: (value) =>
        value.length < 2 ? "Must be longer than two characters" : null,
      content: (value) =>
        value.length < 3 ? "Content must have at least 3 characters" : null,
    },
  });

  return <PostEdit form={form} error={error} />;
}

export const Empty: Story = {
  render: (args) => (
    <PostEditWrapper
      initialValues={{
        title: "",
        content: "",
        id: "",
      }}
      error={null}
      {...args}
    />
  ),
  // @ts-expect-error - args intentionally broader than StoryObj type
  args: {},
};

export const WithContent: Story = {
  render: (args) => (
    <PostEditWrapper
      initialValues={{
        title: "Getting Started with React",
        content:
          "<p>React is a JavaScript library for building user interfaces with reusable components.</p><p>Here are some key features:</p><ul><li>Component-based</li><li>Virtual DOM</li><li>Declarative</li></ul>",
        id: "123",
      }}
      error={null}
      {...args}
    />
  ),
  // @ts-expect-error - args intentionally broader than StoryObj type
  args: {},
};

export const WithError: Story = {
  render: (args) => (
    <PostEditWrapper
      initialValues={{
        title: "My Post",
        content: "<p>Some content</p>",
        id: "456",
      }}
      error="Failed to save post. Please try again."
      {...args}
    />
  ),
  // @ts-expect-error - args intentionally broader than StoryObj type
  args: {},
};

export const WithFormattedContent: Story = {
  render: (args) => (
    <PostEditWrapper
      initialValues={{
        title: "Advanced Styling Techniques",
        content: `<h2>Introduction</h2><p>Learn advanced styling techniques for modern web development.</p><h3>CSS Grid</h3><p>CSS Grid is a powerful layout system that allows you to create complex layouts with ease.</p><blockquote><p>CSS Grid changes how we think about layout.</p></blockquote><pre><code class="language-css">.container { display: grid; grid-template-columns: 1fr 1fr 1fr; }</code></pre><h3>Color Schemes</h3><p>Choose colors carefully to create a cohesive design. <strong>Bold text</strong> and <em>italic text</em> can emphasize important points.</p>`,
        id: "789",
      }}
      error={null}
      {...args}
    />
  ),
  // @ts-expect-error - args intentionally broader than StoryObj type
  args: {},
};
