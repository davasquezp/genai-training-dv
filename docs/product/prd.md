# PRD: LatinVibe MVP (Interest + Communities)

## Summary
LatinVibe MVP vision is to create a one-stop-shop for dancing communities globally. Goal is to link communitity events and local dancing actors with the dancers. Idea is to create a value proposition for dancers across the globe and monetize the activity with tiered capabilities as well as with commission based payments. 

## Goals
### Capture dancer interest with minimal friction

- Collect role + styles to understand demand per community/region.

### Enable communities (city/area) with landing pages and communication links

- Publish community channels (WhatsApp/Email/Telegram/Instagram).
- Surface related items (Events/Parties/Schools/Instructors) as links/associations.

### Provide organizers with visibility into registrations and communities

- Enable basic listing/insights to validate demand and growth.

### Enable monetization for the dancing community via payments

- Tiered communities: charge for creating communities with specific features/capabilities in a tiered manner.
- Commission-based ticketing: take a commission via payment gateways for event ticketing such as festivals (including accommodation) or workshops.

## Non-goals (MVP)
- Full authentication / accounts
- Matching algorithm
- Messaging/chat
- Payments (implementation)
- Full event scheduling (may be linked as a related feature later)

## Target users
- **Dancers**: want to express interest and preferences (role/styles).
- **Community admins**: want a simple landing page and to centralize links/channels.

## MVP scope

### Dancer interest
- Form captures:
  - name
  - role (lead/follower)
  - country (code + name)
  - styles (multi-select; e.g. Bachata/Salsa/Kizomba/Zouk)
- Admin/debug listing to verify submissions.
- Persistence:
  - in-memory initially (upgrade path to durable storage)

### Communities

#### Community entity
- Has a **name**
- Has a **region** (city/area)
- Has **one or more admins**
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
- A dancer can submit their interest and receives a clear confirmation that it was received.
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

