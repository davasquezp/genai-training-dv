# PRD: LatinVibe MVP (Interest + Communities)

## Summary
LatinVibe MVP vision is to create a one-stop-shop for dancing communities globally. Goal is to link communitity events and local dancing actors with the dancers. Idea is to create a value proposition for dancers across the globe and monetize the activity with tiered capabilities as well as with commission based payments. 

## Goals
### Capture dancer interest with minimal friction

- Collect role + styles to understand demand per community/region.

### Enable member accounts (signup/login) for role-based experiences

- Let users create accounts with unique email + phone.
- Support simple login to unlock role-based actions (starting with dancers).
- Provide a **My profile** area for logged-in members to manage their account and activate dancer experiences.

### Enable communities (city/area) with landing pages and communication links

- Publish community channels (WhatsApp/Email/Telegram/Instagram).
- Surface related items (Events/Parties/Schools/Instructors) as links/associations.
- Communities can be in created,activated, deactivated statuses.

### Provide organizers with visibility into registrations and communities

- Enable basic listing/insights to validate demand and growth.

### Enable monetization for the dancing community via payments

- Tiered communities: charge for creating communities with specific features/capabilities in a tiered manner.
- Commission-based ticketing: take a commission via payment gateways for event ticketing such as festivals (including accommodation) or workshops.

## Secondary goals (MVP)
- Matching algorithm
- Messaging/chat
- Full event scheduling (may be linked as a related feature later)

## Target users
- **Dancers**: want to express interest and preferences (role/styles).
- **Community admins**: want a simple landing page and to centralize links/channels.

## MVP scope

### User management (Members)
- Domain entity: **Member**
- Roles:
  - None
  - Dancer (activated by the member from My profile)
  - CommunityManager
  - Instructor
- Signup:
  - email (unique)
  - phone (required, unique)
  - password (stored as **hash + salt**)
- Login:
  - email + password
  - yields an authenticated session/token for the web app
- Authorization-based UI (initial):
  - A **Join community** button is visible only for an authenticated member with role **Dancer**

### Member profile (My profile)
- A logged-in member can access a **My profile** page.
- The My profile page is only visible for the **currently logged-in member**.
- From My profile, the member can activate dancer experiences with an **I am a dancer** action.
- After activation:
  - The UI shows a confirmation message (**Now you are a dancer!**)
  - The UI offers a button to navigate to dancer details (a dedicated Dancer tab/section).

### Dancer onboarding (after activation)
- Dancer preferences are managed from a Dancer tab within My profile after dancer activation.
- If the dancer profile is not available yet, the Dancer tab shows **Your dancer profile is not yet available**.
- If the dancer profile exists, the member can view and edit their dancer details:
  - Nationality
  - Dancer role (Lead/Follower/None)

### Communities

#### Community entity
- Has a **name**
- Has a **region** (city/area)
- Has **one or more admins**
- Has a **cover image** (landscape 4:3 ratio)
- Has a **landing page**
- Can publish **communication links**:
  - WhatsApp
  - Email
  - Telegram
  - Instagram

#### Dancer ↔ community association
- A dancer can belong to **zero or many** communities.

#### Tiered limits
- Communities have tiered limits on how many dancers can belong to them.
- Tier model details (exact tiers/limits/pricing) can be defined later; MVP needs:
  - a way to represent a tier
  - enforcement of a max members value per community

#### Related features (associations)
Communities can be associated with related features such as:
- Events
- Parties
- Schools
- Instructors

MVP requirement is to represent/link these (not to build full modules).

## Acceptance criteria
- A user can sign up with a unique email and phone number and receives a clear confirmation that the account was created.
- A user can log in with valid credentials.
- A logged-in member can access **My profile** and see their account details.
- A member can press **I am a dancer** and receives a confirmation message without immediate navigation.
- After becoming a dancer, the member can open the Dancer tab and:
  - See **Your dancer profile is not yet available** if no dancer profile exists yet.
  - Edit and save dancer details when a dancer profile exists.
- An authenticated member with role **Dancer** can see a **Join community** button on a community page.
- Organizers can view registrations for a community/region to validate demand.
- A community has a public landing page that shows its name, region, and communication links.
- A community cannot exceed its configured tier limit for members.

## Metrics
- Registrations per week
- Submit success rate (vs failures)
- Number of communities created
- % of communities with at least 2 links configured

## Dependencies
- Deployed backend and frontend

## Risks & mitigations
- **Deployment misconfiguration** → document and standardize the deployment process.
- **Data loss with in-memory persistence** → prioritize durable persistence soon after MVP validation.
- **Admin ownership/auth** → start with minimal admin concept; add auth in a later milestone.

## Out of scope decisions (defer)
- Pricing and tier names
- Admin authentication model
- Community moderation policies

